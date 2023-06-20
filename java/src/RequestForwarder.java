import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;

public class RequestForwarder implements InterfaceService {


    /**
     * This method is used to forward a request to the right service
     *
     * format payload {
     *  "url": "something"
     * }
     * @param payload the request to forward
     * @return
     * @throws RemoteException
     */

    @Override
    public JSONObject request(JSONObject payload) throws RemoteException {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject response = null;
        try {
            response = new JSONObject(client.send(
                    HttpRequest.newBuilder(
                            URI.create(payload.getString("url"))
                    ).build(),
                    HttpResponse.BodyHandlers.ofString()
            ).body());
        } catch (Exception ignored) {
        }
        return response;
    }
}
