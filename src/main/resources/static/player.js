const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/users', (response) => {
    showMessage(JSON.parse(response.body));
  });
});

function showMessage(message) {
  $("#message").html("");
  for (var i in message) {
    $("#message").append("<tr><td>" + message[i]["userName"] + "</td></tr>")
  }
}

fetch('/api/user')
  .then(response => { return response.json() })
  .then(result => { showMessage(result) });
