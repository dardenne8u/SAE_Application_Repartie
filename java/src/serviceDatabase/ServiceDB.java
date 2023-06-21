
import org.json.JSONObject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class ServiceDB implements Service {

    private static Connection db;
    private static String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";


    @Override
    public String request(String data) throws RemoteException {
        JSONObject jsonObject = new JSONObject(data);
        switch(jsonObject.getString("action")) {
            case "select":
                try {
                    return select(jsonObject.getJSONObject("data"));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return new JSONObject("{ status: { action: select, success: false } }").toString();
                }
            case "insert":
                try {
                    insert_reservation(jsonObject.getJSONObject("data"));
                    return new JSONObject("{ status: { action: insert, success: true } }").toString();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return new JSONObject("{ status: { action: insert, success: false } }").toString();
                }
            case "delete":
                try {
                    remove_reservation(jsonObject.getJSONObject("data"));
                    return new JSONObject("{ status: { action: delete, success: true } }").toString();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return new JSONObject("{ status: { action: insert, success: false } }").toString();
                }
            case "update":
                try {
                    update_reservation(jsonObject.getJSONObject("data"));
                    return new JSONObject("{ status: { action: update, success: true } }").toString();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return new JSONObject("{ status: { action: insert, success: false } }").toString();
                }
            default:
                return new JSONObject("{ status: \"action inconnu\" }").toString();

        }
    }

    private String select(JSONObject data) throws SQLException {
        PreparedStatement statement = db.prepareStatement("SELECT * FROM " + data.getString("table"));
        statement.execute();

        ResultSet result = statement.getResultSet();
        JSONObject response = new JSONObject();
        while (result.next()) {
            JSONObject row = new JSONObject();
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                row.put(result.getMetaData().getColumnName(i), result.getString(i));
            }
            response.put(result.getString(1), row);
        }
        return response.toString();
    }

    private void insert_reservation(JSONObject data) throws SQLException {
        PreparedStatement state = db.prepareStatement("SELECT count(*) FROM reservation ");
        state.execute();
        ResultSet result = state.getResultSet();
        result.next();
        PreparedStatement statement = db.prepareStatement("insert into " +
                "reservation " +
                "values (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?)");
        statement.setInt(1, result.getInt(1) + 1);
        statement.setInt(2, data.getInt("idRestaurant"));
        statement.setString(3, data.getString("nomClient"));
        statement.setInt(4, data.getInt("numTable"));
        statement.setString(5, data.getString("dateReservation"));
        statement.setInt(6, data.getInt("nbPersonnes"));
        statement.executeUpdate();
    }

    private void remove_reservation(JSONObject data) throws SQLException {
        PreparedStatement statement = db.prepareStatement("delete from reservation where idReservation = ?");
        statement.setInt(1, data.getInt("idReservation"));
        statement.executeUpdate();
    }

    private void update_reservation(JSONObject data) throws SQLException {
        PreparedStatement statement = db.prepareStatement("update reservation set " +
                "IDRESTAURANT = ?, nomClient = ?, numTable = ?, DATERESERVATION = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), nbPersonnes = ? " +
                "where IDRESERVATION = ?");
        statement.setInt(1, data.getInt("idRestaurant"));
        statement.setString(2, data.getString("nomClient"));
        statement.setInt(3, data.getInt("numTable"));
        statement.setString(4, data.getString("dateReservation"));
        statement.setInt(5, data.getInt("nbPersonnes"));
        statement.setInt(6, data.getInt("idReservation"));
        statement.executeUpdate();

    }

    public static void main(String[] args) throws SQLException {
        String server = "localhost";
        int port = 1099;

        // If there are arguments
        if (args.length > 0) {
            // Help
            if(args[0].equals("help")) {
                System.out.println("Usage: java -jar ServiceDB.jar [server = localhost] [port = 1099]");
                System.exit(0);
            }
            // Server
            server = args[0];
            // Port
            if (args.length > 1)
                port = Integer.parseInt(args[1]);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(server, port);
        } catch (RemoteException e) {
            System.out.println("The server " + server + ":" + port + " is not available");
            System.exit(1);
        }

        Service service = null;
        try {
            service = (Service) UnicastRemoteObject.exportObject(new ServiceDB(), 0);
        } catch (RemoteException e) {
            System.out.println("The service cannot be exported");
            System.exit(1);
        }

        Proxy proxy = null;
        try {
            proxy = (Proxy) registry.lookup("proxy");
        } catch (Exception e) {
            System.out.println("The proxy doesn't exist");
            System.exit(1);
        }

        try {
            proxy.register("database",service);
        } catch (RemoteException e) {
            System.out.println("The service cannot be registered");
            System.exit(1);
        }
        db = DriverManager.getConnection(url,Config.username, Config.password);
        System.out.println("ServiceDB is ready");
    }


}
