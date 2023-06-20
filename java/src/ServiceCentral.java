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
import java.util.HashMap;
import java.util.Map;

public class ServiceCentral implements HttpHandler, InterfaceCentral {
    private static final String BEGIN_PATH = "/sae";
    private Map<String, InterfaceService> services = new HashMap<String, InterfaceService>();
    //private ForwarderInterface forwarder;

    @Override
    public void handle(HttpExchange request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getRequestBody());
        String body = new String(bis.readAllBytes());


        System.out.println(body);
        String response = redirection("", request.getRequestMethod(), body);


        request.sendResponseHeaders(200, response.length());
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String redirection(String path, String method, String stringData) {
        String response;

        path = path.substring(BEGIN_PATH.length());
        JSONObject data = new JSONObject(stringData);

        if (method.equals("GET"))
            response = getRequest(path, data);
        else if (method.equals("POST"))
            response = postRequest(path, data);
        else
            response = "Method not allowed";

        return response;
    }
    private String postRequest( String path, JSONObject data) {
        switch(path) {
            case "/search":
                try {
                    services.get("forwarder").request(data);
                } catch (RemoteException e) {
                    System.out.println("The forwarder service is not available");
                }
                break;
            case "/restaurant":
                try {
                    services.get("database").request(data);
                } catch (RemoteException e) {
                    System.out.println("The database service is not available");
                }
                break;
        }

        return "";
    }

    private String getRequest( String path, JSONObject data) {
        switch(path) {

        }
        return "";
    }


  public static void main(String[] args) {
    HttpServer server = null;
    try {
      // Creation de serveur (c'est pas dit dans la methode sur le port 8080)
      server = HttpServer.create(new InetSocketAddress(8080), 0);
    } catch (IOException e) {
      System.out.println("The port is already used");
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
            System.out.println("Erreur ici : " + e.getMessage());
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
        services.put(name, service);
    }
}
