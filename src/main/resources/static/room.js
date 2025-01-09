const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({ roomId: $("#roomId") }, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/room/' + $("#roomId"), (response) => {
    getData();
  });
  stompClient.subscribe('/topic/startGame/' + $("#roomId").text(), (response) => {
    const params = {
      id: $("#userid").text(),
      roomId: $("#roomId").text()
    };
    const query = new URLSearchParams(params);
    window.location.href = `/game?${query.toString()}`;
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
  var el = document.getElementById("roomId");
  var roomId = el.textContent;
  fetch("/api/user/room?roomId=" + roomId)
    .then(response => { return response.json() })
    .then(result => { showMessage(result) });
}

function init() {
  const roomId = $("#roomId").text();
  window.location.href = `/init?roomId=${roomId}`;
}

document.addEventListener("DOMContentLoaded", () => {
  const button = document.getElementById('init');
  button.setAttribute('onclick', 'init()');
  getData();
});
