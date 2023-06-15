package gps;

public abstract class Lieu {
    private int id;
    private String nom;
    private Coordonnees coordonnees;
    private int codePostal;
    private String adresse;

    public Lieu(int id, String nom, Coordonnees coordonnees, int codePostal, String adresse) {
        this.id = id;
        this.nom = nom;
        this.coordonnees = coordonnees;
        this.codePostal = codePostal;
        this.adresse = adresse;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public String getAdresse() {
        return adresse;
    }

    //setters
    public void setNom(String nom) {
        this.nom = nom;
    }
}
