package gps;

public class Restaurant extends Lieu{
    private int numTel;

    public Restaurant(int id, String nom, Coordonnees coordonnees, int codePostal, String adresse, String num) {
        super(id, nom, coordonnees, codePostal, adresse);
        try {
            num = num.replaceAll("\\s+","");
            this.numTel = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            System.out.println("Le numéro de téléphone n'est pas un nombre");
        }
    }
}
