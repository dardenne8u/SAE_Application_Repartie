import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ForwarderLauncher {

    public static void main(String[] args) {
        int port = 1099;
        String host = "localhost";
        if(args.length > 0) host = args[0];
        if(args.length > 1) port = Integer.parseInt(args[1]);



        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(host, port);
        } catch (Exception e) {
            System.out.println("Erreur ici : " + e.getMessage());
            System.exit(1);
        }

        InterfaceService n = null;
        try {
            n = (InterfaceService) UnicastRemoteObject.exportObject(new RequestForwarder(), 0);
        } catch (RemoteException e) {
            System.out.println("Cannot cast to remote object");
            System.exit(1);
        }

        try {
            InterfaceCentral d = (InterfaceCentral) registry.lookup("serviceCentral");
            d.registerService("forwarder",n);
        } catch (Exception e) {
            System.out.println("Erreur la : " + e.getMessage());
            System.exit(1);
        }
    }
}
