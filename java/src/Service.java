import gps.Coordonnees;
import gps.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    ServiceCentral serviceCentral;
    Connection con;
    Statement stmt;

    public Service(ServiceCentral serviceCentral) {
        this.serviceCentral = serviceCentral;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SAE", "root", "root");
            stmt = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données");
        }
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restos = new ArrayList<Restaurant>();

        String query = "SELECT * FROM Restaurant";

        ResultSet results;

        try {
            results = stmt.executeQuery(query);

            while (results.next()) {
                int id = results.getInt("id");
                String nom = results.getString("nom");
                double latitude = results.getDouble("latitude");
                double longitude = results.getDouble("longitude");
                int codePostal = results.getInt("codePostal");
                String adresse = results.getString("adresse");
                String num = results.getString("numTel");

                Coordonnees coordonnees = new Coordonnees(latitude, longitude);
                Restaurant resto = new Restaurant(id, nom, coordonnees, codePostal, adresse, num);
                restos.add(resto);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données");
        }

        return restos;
    }
}
