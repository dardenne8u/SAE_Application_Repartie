import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class ServiceCentral implements HttpHandler, InterfaceCentral {
    private static final String BEGIN_PATH = "/sae";
    private Map<String, InterfaceService> services = new HashMap<>();

    @Override
    public void handle(HttpExchange request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getRequestBody());
        String body = new String(bis.readAllBytes());
        if (body.equals("")) {
            body = "{}";
        }

        request.getResponseHeaders().add("Content-Type", "application/json");
        JSONObject jsonObject;
        try {
            jsonObject = redirection(request.getRequestURI().getPath(), body);
            request.sendResponseHeaders(200, jsonObject.toString().length());
        } catch (ServiceNotAvailableException e) {
            jsonObject = new JSONObject("{ error : " + e.getMessage() + " }");
            request.sendResponseHeaders(500, jsonObject.toString().length());
        }

        String response = jsonObject.toString();
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private JSONObject redirection(String path, String stringData) throws ServiceNotAvailableException {
        JSONObject response;

        JSONObject data = new JSONObject(stringData);

        switch(path) {
            case "/sae/search":
                try {
                    response = services.get("forwarder").request(data);
                } catch (RemoteException e) {
                    throw new ServiceNotAvailableException("The forwarder service is not available");
                }
                break;
            case "/sae/restaurant":
                try {
                    response = services.get("database").request(data);
                } catch (RemoteException e) {
                    throw new ServiceNotAvailableException("The database service is not available");
                }
                break;
            case "/sae":
                response = new JSONObject("{ ok : ok }");
                break;
            default:
                throw new ServiceNotAvailableException("The service " + path + " is not available");
        }

        return response;
    }


  public static void main(String[] args) {
    HttpServer server = null;
    try {
      // Creation de serveur (c'est pas dit dans la methode sur le port 8080)
      server = HttpServer.create(new InetSocketAddress(8080), 0);
    } catch (IOException e) {
      System.out.println("The port is already used HTTP");
      System.exit(1);
    }

        // Ajoute un contexte donc en gros : http://localhost:8080/sae
        server.createContext(BEGIN_PATH, new ServiceCentral());

        // Et ca on s'en fou
        server.setExecutor(null);
        server.start();
    }
    public ServiceCentral()  {
        int port = 1099;

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            System.out.println("The port is already used RMI");
            System.exit(1);
        }

        InterfaceCentral i = null;
        try {
            i = (InterfaceCentral) UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            System.out.println("Cannot cast to remote object");
            System.exit(1);
        }

        try {
            registry.rebind("serviceCentral", i);
        } catch (Exception e) {
            System.out.println("Erreur la : " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void registerService(String name, InterfaceService service) throws RemoteException {
        System.out.println("Registering service " + name);
        services.put(name, service);
    }
}
