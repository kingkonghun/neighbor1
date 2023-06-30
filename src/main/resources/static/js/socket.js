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
            showTopNotification(JSON.parse(chat.body).sender, JSON.parse(chat.body).senderName, JSON.parse(chat.body).content, JSON.parse(chat.body).messageDate, JSON.parse(chat.body).roomId);
            refreshChatList();
        });

        stompClient.subscribe("/topic/message/" + roomId, function (chat) {
            showMessage(JSON.parse(chat.body).sender, JSON.parse(chat.body).senderName, JSON.parse(chat.body).content, JSON.parse(chat.body).messageDate, JSON.parse(chat.body).messageType);
        });
    });
}

function showMessage(sender, senderName, content, messageDate, messageType) {
    var myId = $("#sender").val();
    if(messageType == "SEND") {
        if(sender == myId) {
            $("#messages").prepend("<div><div class='message my_message'><p>" + content + "<br><span>" + messageDate + "</span></p></div></div>");

        }else {
            $("#messages").prepend("<div class='message frnd_message'><div class='user_profile'><img class='room_cover' src='/member/findProfileImg?memberId="+sender+"'><span>" + senderName + "</span></div><p>" + content + "<br><span>" + messageDate + "</span></p></div>");
        }
    }else if(messageType == "ENTER") {
        $("#messages").prepend("<div class='message1 join'><p>" + content + "</p></div>");
    }

}
function showTopNotification(sender, senderName, content, messageDate, roomId) {
    $(".popupup").css("display", "flex");
    $(".userImg_").attr("src", "/member/findProfileImg?memberId="+sender);
    $("#popupName").text(senderName);
    var date = messageDate.substr(11, 9);
    $(".calltime").text(date);
    $("#popup_message").text(content);
    $(".popupup").fadeIn();
    $("#popupRoomId").val(roomId);
    setTimeout(function() {
        $(".popupup").fadeOut();
    }, 3000);
}

function refreshChatList() {
    $("#chatRoomList").load(location.href+' #chatRoomList');
}

function sendPrivateMessage() {
    var content = $("#message").val();
    var receiver = $("#receiver").val();
    var roomId = $("#roomId").val();
    console.log("sending private message");
    stompClient.send("/ws/message", {}, JSON.stringify({"roomId": roomId, "receiver": receiver, "content": content, "messageType": "SEND"}));
}
