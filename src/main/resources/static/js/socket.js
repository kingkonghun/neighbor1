var stompClient = null;
$(function(){
    console.log("Index page is ready");
    connect();

    $("#send").click(function() {
        sendMessage();
        $("#message").val('');
    });

    $("#message").keydown(function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            sendMessage();
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
            showTopNotification(JSON.parse(chat.body).sender, JSON.parse(chat.body).senderName, JSON.parse(chat.body).content, JSON.parse(chat.body).messageDate, JSON.parse(chat.body).roomId, JSON.parse(chat.body).type);
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
function showTopNotification(sender, senderName, content, messageDate, roomId, type) {
    $(".popupup").fadeIn();
    $(".popupup").css("display", "flex");
    $(".userImg_").attr("src", "/member/findProfileImg?memberId="+sender);
    $("#popupName").text(senderName);
    var date = messageDate.substr(11, 9);
    $(".calltime").text(date);
    $("#popup_message").text(content);
    $("#popupRoomId").val(roomId);
    $("#popupType").val(type);
    setTimeout(function() {
        $(".popupup").fadeOut();
    }, 3000);
}

function refreshChatList() {
    $("#chatRoomList").load(location.href+' #chatRoomList');
}

function sendMessage() {
    var content = $("#message").val();
    var roomId = $("#roomId").val();
    var type = $("#type").val();
    console.log("sending used message");
    stompClient.send("/ws/message", {}, JSON.stringify({"roomId": roomId, "content": content, "messageType": "SEND", "type": type}));
}