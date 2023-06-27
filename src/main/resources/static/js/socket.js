var stompClient = null;
$(function(){
    console.log("Index page is ready");
    connect();

    $("#send").click(function() {
        sendPrivateMessage();
        $("#message").val('');
    });

    $("#message").keydown(function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            sendPrivateMessage();
            $("#message").val('');
        }
    });
});


function connect() {
    var socket = new SockJS("/our-websocket");
    var roomId = $("#roomId").val();
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);
        stompClient.subscribe("/user/topic/messageNotification", function (chat) {
            showTopNotification(JSON.parse(chat.body).senderName, JSON.parse(chat.body).content);
            refreshChatList();
        });

        stompClient.subscribe("/topic/message/" + roomId, function (chat) {
            showMessage(JSON.parse(chat.body).sender, JSON.parse(chat.body).content, JSON.parse(chat.body).messageDate);
        });
    });
}

function showMessage(sender, content, messageDate) {
    var myId = $("#sender").val();
    if(sender == myId) {
        $("#messages").prepend("<div><div class='message my_message'><p>" + content + "<br><span>" + messageDate + "</span></p></div></div>");

    }else {
        $("#messages").prepend("<div><div class='message frnd_message'><p>" + content + "<br><span>" + messageDate + "</span></p></div></div>");
    }
}
function showTopNotification(senderName, content) {
    $("#notification").text(senderName + "님 : " + message);
    $("#notification").fadeIn();

    setTimeout(function() {
    $("#notification").fadeOut();
    }, 1500);
}

function refreshChatList() {
    $("#chatRoomList").load(location.href+' #chatRoomList');
}

function sendPrivateMessage() {
    var content = $("#message").val();
    var receiver = $("#receiver").val();
    var roomId = $("#roomId").val();
    console.log("sending private message");
    stompClient.send("/ws/message", {}, JSON.stringify({"roomId": roomId, "receiver": receiver, "content": content}));
}
