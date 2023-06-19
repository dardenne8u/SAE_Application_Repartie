import com.sun.net.httpserver.HttpHandler;

import java.rmi.Remote;

public interface InterfaceCentral extends HttpHandler, Remote {

    public void RegisterForwarder(ForwarderInterface forwarder) throws java.rmi.RemoteException;
}
