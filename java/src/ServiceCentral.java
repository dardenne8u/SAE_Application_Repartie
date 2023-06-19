import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class ServiceCentral implements InterfaceCentral {
  private Map<String,Service> services = new HashMap<>();
  private ForwarderInterface forwarder;

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
    System.out.println("");
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

  @Override
  public void RegisterForwarder(ForwarderInterface forwarder) throws RemoteException {
    this.forwarder = forwarder;
  }

  public void addService(String name, Service service) {
    services.put(name, service);
  }

    public ServiceCentral() {
      int port = 1099;
      InterfaceCentral n = this;

      Registry registry = null;
      try {
        registry = LocateRegistry.createRegistry(port);
      } catch (Exception e) {
        System.out.println("Erreur ici : " + e.getMessage());
        System.exit(1);
      }

      try {
        registry.rebind("Serv", n);
      } catch (Exception e) {
        System.out.println("Erreur la : " + e.getMessage());
        System.exit(1);
      }
    }

  public void setForwarder(ForwarderInterface forwarder) {
    this.forwarder = forwarder;
  }
}
