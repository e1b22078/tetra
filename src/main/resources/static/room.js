const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/roomusers', () => {
    getData();
  });
  stompClient.subscribe('/topic/startGame/' + $("#roomid").text(), () => {
    console.log("startGame");
    window.location.href = $("#gamestart").attr("href") + "&trigger=true";
  });
});

function showMessage(message) {
  $("#message").html("");
  for (var i in message) {
    $("#message").append("<tr><td>" + message[i]["userName"] + "</td></tr>")
  }
  if (message.length > 1) {
    $("#gamestart").show();
  } else {
    $("#gamestart").hide();
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
