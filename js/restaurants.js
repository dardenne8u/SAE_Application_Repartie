const API = "https://webetu.iutnc.univ-lorraine.fr/dardenne8u/applications/myapp";

const getRestaurantsData = async () => {
    let response = await fetch(API);

    if(!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    let reponse = await response.json();
    return reponse;
}


export default {
    getRestaurantsData
}
