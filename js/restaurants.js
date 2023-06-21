const API = "https://webetu.iutnc.univ-lorraine.fr/dardenne8u/SAERepartie/";


const forwarder = async () => {
    let promise = await fetch(`${API}/forwarder`, {
        method: "POST",
        body: '{ "url" : "https://data.enseignementsup-recherche.gouv.fr//explore/dataset/fr-esr-principaux-etablissements-enseignement-superieur/download?format=json&amp;timezone=Europe/Berlin&amp;use_labels_for_header=false" }',
    });

    if (!promise.ok) {
        console.log("Error");
        return;
    }

    let response = await promise.json();
    console.log(response);
    return reponse;
}

const reserverRestaurant = async (idRestau, nomClient, nbPersonnes) => {
    let dateReserv = new Date().toISOString().slice(0, 19).replace('T', ' ');
    let promise = await fetch(`${URL}/database`, {
        method: "POST",
        body: `{ "action": "insert", "data": { "idReservation":1, "idRestaurant": ${idRestau},"nomClient": "${nomClient}","nbPersonnes": ${nbPersonnes},"dateReservation": "${dateReserv}", numTable: 5}}`
    });

    if (!promise.ok) {
        console.log("Error");
        return;
    }

    let response = await promise.json();
    console.log(response);
}


export default {
    forwarder,
    reserverRestaurant
}
