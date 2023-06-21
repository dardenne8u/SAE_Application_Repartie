const API = "https://webetu.iutnc.univ-lorraine.fr/dardenne8u/SAERepartie/";

const recupererRestaurants = async () => {
    let promise = await fetch(`${URL}/database`, {
        method: "POST",
        body: `{ "action": "select", "data": { "table":"restaurant"}}`
    });

    if (!promise.ok) {
        console.log("Error");
        return;
    }

    return await promise.json();
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
    reserverRestaurant,
    recupererRestaurants
}
