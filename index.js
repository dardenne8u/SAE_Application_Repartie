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

}

printStationsInformation()
