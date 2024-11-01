var clientSocket = Stomp.client("ws://localhost:8080/ws");

// connect to WebSocket using STOMP
clientSocket.connect({}, connectionCallback, errorCallback);

// connection Callback
function connectionCallback(){
    clientSocket.subscribe("/topic/occupancy", subscribeCallback);
}

// error Callback
function errorCallback(e) {
    console.log("Error " + e.data)
}

// subscribe Callback
function subscribeCallback(e){
    console.log(e);
}