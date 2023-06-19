const API = "https://carto.g-ny.org/data/cifs/cifs_waze_v2.json";

const getCirculationData = async () => {
    let response = await fetch(API);

    if(!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    let reponse = await response.json();
    return reponse.incidents;
}


export default {
    getCirculationData
}
