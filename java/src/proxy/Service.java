package proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {

    String request(String data) throws RemoteException;
}
