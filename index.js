import velibs from "./js/velibs.js";
import circulation from "./js/circulation.js";
import restaurants from "./js/restaurants.js";

let urlServeur = "https://webetu.iutnc.univ-lorraine.fr/www/dardenne8u/SAE_ApplicationRepartie/";
let port = "8080";

let map = L.map('map').setView([48.691173, 6.184768], 13);
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

map.on('click', function(ev){
    let latlng = map.mouseEventToLatLng(ev.originalEvent);
    document.querySelector("#lat").value=latlng.lat;
    document.querySelector("#lon").value=latlng.lng;
});

document.querySelector('#validateAjout').onclick = function(){
    restaurants.reserverRestaurant(document.querySelector('#idRes').value,document.querySelector('#nomCli').value,document.querySelector('#nbPers').value);
};



const markers = [];
const markersCircu = [];
const markersRestau = [];

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

const printRestaurants = async () => {
    let restaurants = await restaurants.recupererRestaurants();
    let LeafIcon = L.Icon.extend({
        options: {
            iconSize:     [50, 50],
            iconAnchor:   [26, 47],
            popupAnchor:  [0, -45]
        }
    });
    let icone = new LeafIcon({iconUrl: './img/greenicon.png'});

    restaurants.forEach(restaurant => {
        let marker = L.marker([restaurant.lat, restaurant.lon], {icon: icone}).addTo(map);
        marker.bindPopup(restaurant.name, {color: 'green'});
        markersRestau.push({
            id: restaurant.id,
            marker: marker
        });
    });
}

const printCirculationEvents = async () => {
    let circuData = await circulation.getCirculationData();
    let LeafIcon = L.Icon.extend({
        options: {
            iconSize:     [50, 50],
            iconAnchor:   [26, 47],
            popupAnchor:  [0, -45]
        }
    });
    let icone = new LeafIcon({iconUrl: './img/redicon.png'});

    circuData.forEach(incident => {
        let marker = L.marker(incident.location.polyline.split(" "), {icon: icone}).addTo(map);
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
printRestaurants();
