
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Forwarder implements Service {

    /**
     * This method is used to forward a request to the right service
     *
     * format payload {
     *  "url": "something"
     * }
     * @param payload the request to forward
     * @return
     * @throws RemoteException
     */

    @Override
    public String request(String payload) throws RemoteException {
        System.out.println("forwarding");
        JSONObject data = new JSONObject(payload);
        String response = null;
        ProxySelector proxy = ProxySelector.of(new InetSocketAddress("www-cache.iutnc.univ-lorraine.fr",3128));
        System.out.println("connecting to proxy");
        HttpClient client = HttpClient.newBuilder().proxy(proxy).build();
        try {
            HttpResponse<String> promise = client.send(
                    HttpRequest.newBuilder(
                            URI.create(data.getString("url"))
                    ).build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            response = promise.body();
        } catch (IOException e) {
            System.out.println("The request cannot be sent");
        } catch (InterruptedException e) {
               System.out.println("The request has been interrupted");
        }
        return response;
    }

    public static void main(String[] args) {
        System.out.println("new one");
        String server = "localhost";
        int port = 1099;

        // If there are arguments
        if (args.length > 0) {
            // Help
            if(args[0].equals("help")) {
                System.out.println("Usage: java -jar Forwarder.jar [server = localhost] [port = 1099]");
                System.exit(0);
            }
            // Server
            server = args[0];
            // Port
            if (args.length > 1)
                port = Integer.parseInt(args[1]);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(server, port);
        } catch (RemoteException e) {
            System.out.println("The server " + server + ":" + port + " is not available");
            System.exit(1);
        }

        Service service = null;
        try {
            service = (Service) UnicastRemoteObject.exportObject(new Forwarder(), 0);
        } catch (RemoteException e) {
            System.out.println("The service cannot be exported");
            System.exit(1);
        }

        Proxy proxy = null;
        try {
            proxy = (Proxy) registry.lookup("proxy");
        } catch (Exception e) {
            System.out.println("The proxy doesn't exist");
            System.exit(1);
        }

        try {
            proxy.register("forwarder",service);
        } catch (RemoteException e) {
            System.out.println("The service cannot be registered");
            System.exit(1);
        }
    }
}
