public interface ForwarderInterface extends java.rmi.Remote{

    public String forwardRequest(String url, String data) throws java.rmi.RemoteException;
}
