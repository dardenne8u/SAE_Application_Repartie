
const HOST = "localhost";
const PORT = 8080;
const URL = `https://${HOST}:${PORT}/sae`;

const forwarder = async () => {
    let promise = await fetch(`${URL}/forwarder`, {
        method: "POST",
        body: '{ "url" : "https://data.enseignementsup-recherche.gouv.fr//explore/dataset/fr-esr-principaux-etablissements-enseignement-superieur/download?format=json&amp;timezone=Europe/Berlin&amp;use_labels_for_header=false" }',
    });

    if (!promise.ok) {
        console.log("Error");
        return;
    }

    let response = await promise.json();
    console.log(response);
}

const db = async () => {
    let promise = await fetch(`${URL}/database`, {
        method: "POST",
        body: '{ "action": "update", "data": { "idReservation":1, "idRestaurant": 0,"nomClient": "gregg","nbPersonnes": 5,"dateReservation": "2020-01-01 22:11:00", numTable: 5}}'
    });

    if (!promise.ok) {
        console.log("Error");
        return;
    }

    let response = await promise.json();
    console.log(response);
}
db();
