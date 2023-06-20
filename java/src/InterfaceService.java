import org.json.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceService extends Remote {

    JSONObject request(JSONObject payload) throws RemoteException;

}
