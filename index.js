import velibs from "./js/velibs.js";
import circulation from "./js/circulation.js";

let map = L.map('map').setView([48.691173, 6.184768], 13);
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);



const markers = [];
const markersCircu = [];

const printStationsInformation = async () => {
    let stations = await velibs.getStationsInformation();

    stations.forEach(station => {
        let marker = L.marker([station.lat, station.lon]).addTo(map);
        marker.bindPopup(station.name, {color: 'orange'});
        markers.push({
            id: station.station_id,
            marker: marker
        });
    });

}

const printStationsStatus = async () => {
    let stations = await velibs.getStationsStatus();

    stations.forEach(station => {
        let marker = markers.filter((marker) => marker.id === station.station_id)[0].marker;
        let popup = marker.getPopup();
        popup.setContent(popup.getContent() + `<br>Vélos disponibles : ${station.num_bikes_available}<br>Emplacements disponibles : ${station.num_docks_available}`)
    });
}

const printCirculationEvents = async () => {
    let circuData = await circulation.getCirculationData();
    console.log(circuData);
    circuData.forEach(incident => {
        let marker = L.marker(incident.location.polyline.split(" ")).addTo(map);
        let start = incident.starttime.split("T")[0];
        let end = incident.endtime.split("T")[0];
        marker.bindPopup(`${incident.location.street}<br>${incident.description}<br>Début incident : ${start}<br>Fin incident : ${end}`);
        markersCircu.push({
            id: incident.id,
            marker: marker
        });
    });
}

printStationsInformation()
    .then(()=>printStationsStatus());

printCirculationEvents();
