
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Proxy extends Remote {
    void register(String name, Service service) throws RemoteException;
}
