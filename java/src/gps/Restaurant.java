package gps;

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
}
