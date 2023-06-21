const IP = "localhost";
const PORT = 8080;
const API = `https://${IP}:${PORT}/sae/database`;

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


const UpdateReservation = async (idReservation, idRestau, nomClient, nbPersonnes) => {
  let dateReserv = new Date().toISOString().slice(0, 19).replace('T', ' ');
  let promise = await fetch(`${URL}/database`, {
    method: "POST",
    body: `{ "action": "update", "data": { "idReservation":${idReservation}, "idRestaurant": ${idRestau},"nomClient": "${nomClient}","nbPersonnes": ${nbPersonnes},"dateReservation": "${dateReserv}", numTable: 5}}`
  });

  if (!promise.ok) {
    console.log("Error");
    return;
  }

  let response = await promise.json();
  console.log(response);
}

const DeleteReservation = async (idReservation) => {
  let promise = await fetch(`${URL}/database`, {
    method: "POST",
    body: `{ "action": "delete", "data": { "idReservation":${idReservation}}}`
  });

  if (!promise.ok) {
    console.log("Error");
    return;
  }

  let response = await promise.json();
  console.log(response);
}

const onchange = (value) => {
  console.log("ziz")
  if (value === "reserver un restaurant") {
    var div = document.getElementById("selctRest");
    var html = "<select id='restos'>"
    var restos = recupererRestaurants();
    for (var i = 0; i < restos.length; i++) {
      html += "<option value='" + restos[i].idRestaurant + "'>" + restos[i].nomRestaurant + "</option>";
    }
    html += "</select>";
    div.innerHTML = html;
  }
}


export default {
  reserverRestaurant,
  recupererRestaurants,
  UpdateReservation,
  DeleteReservation,
  onchange

}
