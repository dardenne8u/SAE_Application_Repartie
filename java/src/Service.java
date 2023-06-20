import gps.Coordonnees;
import gps.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    Connection con;
    Statement stmt;
    String name;

    public Service(String name) {
        this.name = name;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SAE", "root", "root");
            stmt = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données");
        }
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restos = new ArrayList<Restaurant>();

        String query = "SELECT * FROM Restaurant INNER JOIN coordonnees ON Restaurant.idRest = coordonnees.idRest";

        ResultSet results;

        try {
            System.out.println("Connecting to database...");
            con = DBConnection.getConnexion();
            System.out.println("Creating statement...");
            stmt = con.createStatement();
            System.out.println("Executing query...");
            results = stmt.executeQuery(query);
            System.out.println("Query executed");

            while (results.next()) {
                System.out.println("Parsing results...");

                //recuperation des attributs de restaurant
                int id = results.getInt("idRest");
                String nom = results.getString("nom");
                int codePostal = results.getInt("codePost");
                String adresse = results.getString("adresse");
                String num = results.getString("tel");
                String ville = results.getString("ville");

                //recuperation des attributs de coordonnees
                double latitude = results.getDouble("latitude");
                double longitude = results.getDouble("longitude");
                Coordonnees coordonnees = new Coordonnees(latitude, longitude);
                Restaurant resto = new Restaurant(id, nom, coordonnees, codePostal, adresse, num, ville);
                restos.add(resto);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return restos;
    }

    public String toJSONRestaurants() {
        String json = "[\n";
        List<Restaurant> restos = getRestaurants();
        for (Restaurant resto : restos) {
            json += resto.toJSON() + ",\n";
        }
        return json.substring(0, json.length() - 2) + "\n]";
    }

    public static void main(String[] args) {
        Service service = new Service("Service1");

        List<Restaurant> restos = service.getRestaurants();

        for (Restaurant resto : restos) {
            System.out.println(resto.getNom());
            System.out.println("====================================");
            System.out.println(resto.toJSON());
            System.out.println("====================================");
            try {
                Connection con = DBConnection.getConnexion();
                resto.reserverTable("[{\"idResa\":1,\"idRest\":1,\"nomClient\":\"Jean\",\"numTable\":\"1\",\"dateResa\":\"2023-06-20 19:36:55\",\"nbPersonnes\":69}]",con);
                resto.modifierReservationTable("[{\"idResa\":1,\"idRest\":1,\"nomClient\":\"Jean\",\"numTable\":\"1\",\"dateResa\":\"2023-06-20 19:36:55\",\"nbPersonnes\":4}]",con);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
