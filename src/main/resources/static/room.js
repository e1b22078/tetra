const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({roomid: $("#roomid")}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/room/' + $("#roomid"), (response) => {
    getData();
  });
  stompClient.subscribe('/topic/startGame/' + $("#roomid").text(), async (response) => {
    console.log("startGame");
    const quiz = JSON.parse(response.body);
    const params = {
      id: $("#userid").text(),
      roomid: $("#roomid").text(),
      word: quiz.word,
      correctMean: quiz.correctMean,
      options: quiz.options.join(','),
      process: quiz.process
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
  var el = document.getElementById("roomid");
  var roomid = el.textContent;
  fetch("/api/user/room?roomid=" + roomid)
    .then(response => { return response.json() })
    .then(result => { showMessage(result) });
}

document.addEventListener("DOMContentLoaded", () => {
  getData();
});
