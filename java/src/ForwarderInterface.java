import com.sun.net.httpserver.HttpExchange;

import java.net.http.HttpResponse;

public interface ForwarderInterface extends java.rmi.Remote{

    public HttpResponse<String> forwardRequest(String url, String method, HttpExchange request) throws java.rmi.RemoteException;
}
