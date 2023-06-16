import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection co;

    private static String url ="jdbc:mysql://webetu.iutnc.univ-lorraine.fr/dardenne8u", usrName = "dessaulx1u", pass = NEPASPUSH.getPass();//mettre nom d'utilisateur et mdp ici


    public static synchronized Connection getConnexion() throws SQLException {//a verifier interet du constructeur prive
        if (co == null) {
            co = DriverManager.getConnection(url,usrName,pass);
        }
        return co;
    }
}