import com.sun.net.httpserver.HttpHandler;

import java.rmi.Remote;

public interface InterfaceCentral extends Remote {

    void registerService(String name,InterfaceService service) throws java.rmi.RemoteException;
}
