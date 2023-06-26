var stompClient = null;

$(function(){
    console.log("Index page is ready");
    connect();

    $("#send").click(function() {
        sendPrivateMessage();
    });

    $("#message").keydown(function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            sendPrivateMessage();
        }
    });
});


function connect() {
    var socket = new SockJS("/our-websocket");
    var roomId = $("#roomId").val();
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);
        stompClient.subscribe("/user/topic/messageNotification", function (message) {
            showTopNotification(JSON.parse(message.body).senderName, JSON.parse(message.body).content);
            refreshChatList();
        });

        stompClient.subscribe("/topic/message/" + roomId, function (message) {
            showMessage(JSON.parse(message.body).receiver, JSON.parse(message.body).content);
        });
    });
}

function showMessage(receiver, message) {
    var receiver_ = $("#receiver").val();
    if(receiver == receiver_) {
        $("#messages").append("<tr><td style='background-color: red;'>" + message + "</td></tr>");
    }else {
        $("#messages").append("<tr><td style='background-color: blue;'>" + message + "</td></tr>");
    }
}
function showTopNotification(senderName, message) {
    $("#notification").text(senderName + "님 : " + message);
    $("#notification").fadeIn();

    setTimeout(function() {
    $("#notification").fadeOut();
    }, 1500);
}

function refreshChatList() {
    $(".chatlist").load(location.href+' .chatlist');
}

function sendPrivateMessage() {
    var content = $("#message").val();
    var receiver = $("#receiver").val();
    var roomId = $("#roomId").val();
    console.log("sending private message");
    stompClient.send("/ws/message", {}, JSON.stringify({"roomId": roomId, "receiver": receiver, "content": content}));
}
