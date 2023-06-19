import com.sun.net.httpserver.HttpExchange;

public interface ForwarderInterface extends java.rmi.Remote{

    public String forwardRequest(String url, String method, HttpExchange request) throws java.rmi.RemoteException;
}
