window.onload = async () =>{
  console.log("ok")
  let responsePromise = await fetch("http://localhost:8080/applications/myapp",{
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  });
  let response = await responsePromise.json();
  console.log(response);
}