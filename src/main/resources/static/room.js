const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/roomusers', () => {
    getData();
  });
  stompClient.subscribe('/topic/startGame/' + $("#roomid").text(), (response) => {
    console.log("startGame");
    const params = { roomid: $("#roomid").text(), id: id };
    const query = new URLSearchParams(params);
    fetch(`/game?${query}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(JSON.parse(response.body)),
    })
  });
});

function showMessage(message) {
  $("#message").html("");
  for (var i in message) {
    $("#message").append("<tr><td>" + message[i]["userName"] + "</td></tr>")
  }
  if (message.length > 1) {
    $("#init").show();
  } else {
    $("#init").hide();
  }
}

function getData() {
  var el = document.getElementById("roomid");
  var roomid = el.textContent;
  fetch("/api/user/room?roomid=" + roomid)
    .then(response => { return response.json() })
    .then(result => { showMessage(result) });
}

document.addEventListener("DOMContentLoaded", () => {
  getData();
});
