import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServiceCentral implements HttpHandler {


  @Override
  public void handle(HttpExchange request) throws IOException {
    /*
     * C'est ici qu'on gere les requete http, faut regarder
     * la documentation de HttpExchange
     * Pour savoir, mais en gros je pense un switch devra faire l'affaire
     * ne pas hésiter à déclarer des methodes privees
     */

    // On recupere les headers de la requete
    for(String s : request.getRequestHeaders().keySet()) {
      System.out.println(s + " : " + request.getRequestHeaders().get(s));
    }
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

    // Ajoute un contexte donc en gros : http://localhost:8080/applications/myapp
    server.createContext("/applications/myapp", new ServiceCentral());

    // Et ca on s'en fou
    server.setExecutor(null);
    server.start();
  }
}
