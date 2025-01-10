const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({ userId: $("#userId") }, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/users', (response) => {
    console.log(JSON.parse(response.body));
  });
});

function showMessage(message) {
  $("#message").html("");
  for (var i in message) {
    $("#message").append("<div>" + message[i]["userName"] + "</td></tr>")
  }
}
