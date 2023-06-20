package proxy;

import com.sun.net.httpserver.*;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Proxy, HttpHandler {

    private static final String BEGIN_PATH = "/sae";

    private Map<String, Service> services = new HashMap<>();

    public Server(int port) {

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.out.println("The port " + port +" is already used (RMI) ");
            System.exit(1);
        }

        Proxy proxy = null;
        try {
            proxy = (Proxy) UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            System.out.println("Problem with the cast to remote object");
            System.exit(1);
        }

        try {
            registry.rebind("proxy", proxy);
        } catch (RemoteException e) {
            System.out.println("Problem with the rebind");
            System.exit(1);
        }
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
        String body = new String(bis.readAllBytes());
        body = body.equals("") ? "{}" : body;

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        String response;
        try {
            response = redirection(exchange.getRequestURI().getPath(), body);
            exchange.sendResponseHeaders(200, response.length());
        } catch (ServiceNotAvailableException e) {
            response = new JSONObject("{ error : \"" + e.getMessage() + "\" }").toString();
            exchange.sendResponseHeaders(404, response.length());
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private String redirection(String path, String data) throws ServiceNotAvailableException {
        String response;

        // If the path is /sae
        if(path.equals(BEGIN_PATH)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("services", new ArrayList<>());
            for (String service : services.keySet()) {
                jsonObject.accumulate("services", new JSONObject().put(service, "/sae/" + service));
            }
            response = jsonObject.toString();
        // If the path is /sae/...
        } else if (path.contains(BEGIN_PATH) && path.length() > BEGIN_PATH.length()) {
            // Get the last word of path corresponding to the service
            path = path.substring(path.lastIndexOf("/")+1);

            // Get the service
            Service service = services.get(path);
            System.out.println(service);
            // If the service doesn't exist
            if (service == null) throw new ServiceNotAvailableException("The service " + path + " doesn't exist");

            // If the service exists
            try {
                response = service.request(data);
            } catch (RemoteException e) {
                // If the service is not available
                System.out.println(e.getMessage());
                services.remove(path);
                throw new ServiceNotAvailableException("The service " + path + " is not available");
            }
        // If the path is not /sae or /sae/...
        } else
            throw new ServiceNotAvailableException("The service " + path + " is not available");

        return response;
    }

    @Override
    public void register(String name, Service service) throws RemoteException {
        System.out.println("Registering " + name);
        services.put(name, service);
    }

    public static void main(String[] args) {

        // Default port
        int portRMI = 1099;
        int portHTTP = 8080;
        if (args.length > 0) {
            // Help
            if (args[0].equals("help")) {
                System.out.println("Usage: java -jar Server.jar [port RMI = 1099] [port HTTP = 8080]");
                System.exit(0);
            }
            // Port
            portRMI = Integer.parseInt(args[0]);

            if (args.length > 1)
                portHTTP = Integer.parseInt(args[1]);

        }

        // Create the proxy
        Server proxy = new Server(portRMI);
        Server.initServer(proxy, portHTTP);
    }

    private static void initServer(HttpHandler proxy, int port) {
        try {
            // setup the socket address
            InetSocketAddress address = new InetSocketAddress(port);

            // initialise the HTTPS server
            HttpsServer httpsServer = HttpsServer.create(address, 0);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // initialise the keystore
            char[] password = "password".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream("src/testkey.jks");
            ks.load(fis, password);

            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            // setup the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        // initialise the SSL context
                        SSLContext context = getSSLContext();
                        SSLEngine engine = context.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        // Set the SSL parameters
                        SSLParameters sslParameters = context.getSupportedSSLParameters();
                        params.setSSLParameters(sslParameters);

                    } catch (Exception ex) {
                        System.out.println("Failed to create HTTPS port");
                    }
                }
            });
            httpsServer.createContext("/sae", proxy);
            httpsServer.setExecutor(null); // creates a default executor
            httpsServer.start();

        } catch (Exception exception) {
            System.out.println("Failed to create HTTPS server on port " + 8000 + " of localhost");
            exception.printStackTrace();

        }
    }
}
