const API = "https://transport.data.gouv.fr/gbfs/nancy/gbfs.json";

const getLinks = async () => {
    let response = await fetch(API);

    if(!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    let data = await response.json();
    return data.data.fr.feeds;
}


const fetchData = async (link) => {
    let response = await fetch(link);

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    let data = await response.json();
    return data.data;
}

const getStationsInformation = async () => {
    let links = await getLinks();
    links = links.filter(link => link.name === "station_information");
    let stationsInformation = await fetchData(links[0].url);
    return stationsInformation.stations;
}

const getStationsStatus = async () => {
    let links = await getLinks();
    links = links.filter(link => link.name === 'station_status');
    let stationsStatus = await fetchData(links[0].url);
    return stationsStatus
}

export default {
    getStationsInformation,
    getStationsStatus
}
