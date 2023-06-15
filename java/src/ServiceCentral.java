import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServiceCentral implements HttpHandler {


    @Override
    public void handle(HttpExchange t) throws IOException {
        

    }

    public static void main(String[] args) {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            System.out.println("The port is already used");
            System.exit(1);
        }

        server.createContext("/applications/myapp", new ServiceCentral());
        server.setExecutor(null);
        server.start();
    }
}
