const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/users/', (response) => {
    console.log(response.body);
    showMessage(JSON.parse(response.body));
  });
});

function showMessage(message) {
  $("#message").html(`<span>${message}</span>`);
}
