import velibs from "./js/velibs.js";

let map = L.map('map').setView([48.691173, 6.184768], 13);
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);



const markers = [];

const printStationsInformation = async () => {
    let stations = await velibs.getStationsInformation();

    stations.forEach(station => {
        let marker = L.marker([station.lat, station.lon]).addTo(map);
        marker.bindPopup(station.name);
        markers.push({
            id: station.station_id,
            marker: marker
        });
    });

}

const printStationsStatus = async () => {
    let stations = await velibs.getStationsStatus();

    stations.forEach(station => {
        let marker = markers.filter(marker=>marker.id===station.id)[0];
        console.log(marker);
        marker.setContent(marker.getContent()+"<br>Nombre de vélos disponibles : " + station.num_bikes_available+ "<br>Nombre de docks disponibles :" + station.num_docks_available);
    });
}

printStationsInformation()
    .then(()=>printStationsStatus());
