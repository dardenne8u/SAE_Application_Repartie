import org.json.JSONArray;
import org.json.JSONObject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Restaurant extends Lieu {
    private int numTel;

    public Restaurant(int id, String nom, Coordonnees coordonnees, int codePostal, String adresse, String num, String ville) {
        super(id, nom, coordonnees, codePostal, adresse, ville);
        try {
            num = num.replaceAll("\\s+", "");
            this.numTel = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            System.out.println("Le numéro de téléphone n'est pas un nombre");
        }
    }

    public String toJSON() {
        return "{\n" +
                "        \"fields\":{\n" +
                "            \"idResto\":" + id + ",\n" +
                "            \"nom_restaurant\":\"" + nom + "\",\n" +
                "            \"geo_point_2d\": [\n" +
                "                " + coordonnees.getLatitude() + ", " + coordonnees.getLongitude() + "\n" +
                "            ],\n" +
                "            \"telephone\":\"" + numTel + "\",\n" +
                "            \"adresse\":\"" + adresse + "\",\n" +
                "            \"code_postal\":" + codePostal + ",\n" +
                "            \"ville\":\"" + ville + "\"\n" +
                "        },\n" +
                "        \"geometry\":{\n" +
                "            \"type\":\"Point\",\n" +
                "            \"coordinates\": [\n" +
                "                " + coordonnees.getLatitude() + ", " + coordonnees.getLongitude() + "\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public void reserverTable(String json, Connection connection) {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray);

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        System.out.println(jsonObject);
        int idResa = jsonObject.getInt("idResa");
        int idRest = jsonObject.getInt("idRest");
        String nomClient = jsonObject.getString("nomClient");
        int numTable = jsonObject.getInt("numTable");
        String date = jsonObject.getString("dateResa");
        int nbPersonnes = jsonObject.getInt("nbPersonnes");

        String formatedDate = "STR_TO_DATE('" + date + "', '%Y-%m-%d %T')";
        String query = "INSERT INTO Reservations (idResa, idRest, nomClient, numTable, dateResa, nbPersonnes) VALUES (" + idResa + ", " + idRest + ", '" + nomClient + "', " + numTable + ", " + formatedDate + ", " + nbPersonnes + ");";
        System.out.println(query);

        try {
            System.out.println("Creating statement...");
            Statement stmt = connection.createStatement();
            System.out.println("Executing query...");
            stmt.executeUpdate(query);
            System.out.println("Query executed");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void modifierReservationTable(String json, Connection connection) {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray);

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        System.out.println(jsonObject);
        int idResa = jsonObject.getInt("idResa");
        int idRest = jsonObject.getInt("idRest");
        String nomClient = jsonObject.getString("nomClient");
        int numTable = jsonObject.getInt("numTable");
        String date = jsonObject.getString("dateResa");
        int nbPersonnes = jsonObject.getInt("nbPersonnes");

        String formatedDate = "STR_TO_DATE('" + date + "', '%Y-%m-%d %T')";
        String query = "UPDATE Reservations SET idRest = " + idRest + ", nomClient = '" + nomClient + "', numTable = " + numTable + ", dateResa = " + formatedDate + ", nbPersonnes = " + nbPersonnes + " WHERE idResa = " + idResa + ";";
        System.out.println(query);

        try {
            System.out.println("Creating statement...");
            Statement stmt = connection.createStatement();
            System.out.println("Executing query...");
            stmt.executeUpdate(query);
            System.out.println("Query executed");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public JSONObject request(JSONObject payload) throws RemoteException {
        JSONObject response = new JSONObject();
        String action = payload.getString("action");
        //TODO: there
        String json = payload.getString("json");
        Connection connection = null;
        try {
            connection = DBConnection.getConnexion();
        } catch (SQLException e) {
            System.out.println(e);
        }

        switch (action) {
            case "reserverTable":
                reserverTable(json, connection);
                break;
            case "modifierReservationTable":
                modifierReservationTable(json, connection);
                break;
            default:
                System.out.println("Action inconnue");
                break;
        }

        return response;
    }

    public static void main(String[] args) {
        int port = 1099;
        String host = "localhost";
        if (args.length > 0) host = args[0];
        if (args.length > 1) port = Integer.parseInt(args[1]);

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(host, port);
        } catch (Exception e) {
            System.out.println("Erreur ici : " + e.getMessage());
            System.exit(1);
        }

        InterfaceService n = null;
        try {
            n = (InterfaceService) UnicastRemoteObject.exportObject(new RequestForwarder(), 0);
        } catch (RemoteException e) {
            System.out.println("Cannot cast to remote object");
            System.exit(1);
        }

        try {
            InterfaceCentral d = (InterfaceCentral) registry.lookup("serviceCentral");
            d.registerService("restaurant", n);
        } catch (Exception e) {
            System.out.println("Erreur la : " + e.getMessage());
            System.exit(1);
        }
    }
}
