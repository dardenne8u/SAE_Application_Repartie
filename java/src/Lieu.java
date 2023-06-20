public abstract class Lieu implements InterfaceService {
    protected int id;
    protected String nom;
    protected Coordonnees coordonnees;
    protected int codePostal;
    protected String adresse;
    protected String ville;

    public Lieu(int id, String nom, Coordonnees coordonnees, int codePostal, String adresse, String ville) {
        this.id = id;
        this.nom = nom;
        this.coordonnees = coordonnees;
        this.codePostal = codePostal;
        this.adresse = adresse;
        this.ville = ville;
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

    abstract public String toJSON();
}
