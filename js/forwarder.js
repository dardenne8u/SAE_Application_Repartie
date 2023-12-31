const IP = "100.64.80.74";
const PORT = 8080;
const API = `https://${IP}:${PORT}/sae/forwarder`;
const URL = "https://data.enseignementsup-recherche.gouv.fr//explore/dataset/fr-esr-principaux-etablissements-enseignement-superieur/download?format=json&amp;timezone=Europe/Berlin&amp;use_labels_for_header=false";

const forwarder = async () => {
    let promise = await fetch(`${API}/forwarder`, {
        method: "POST",
        body: `{ "url" : "${URL}" }`,
    });

    if (!promise.ok) {
        console.log("Error");
        return;
    }

    let response = await promise.json();
    console.log(response);
    return response;
}

export default {
    forwarder
}
