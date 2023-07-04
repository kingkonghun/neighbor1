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
            showMessage(JSON.parse(chat.body).sender, JSON.parse(chat.body).senderName, JSON.parse(chat.body).content, JSON.parse(chat.body).messageDate, JSON.parse(chat.body).messageType, JSON.parse(chat.body).memberCount, JSON.parse(chat.body).nowMan, JSON.parse(chat.body).maxMan);
        });
    });
}

function showMessage(sender, senderName, content, messageDate, messageType, memberCount, nowMan, maxMan) {
    var myId = $("#sender").val();
    var myName = $("#senderName").val();
    if(messageType == "SEND") {
        if(sender == myId) {
            $("#messages").prepend("<div><div class='message my_message'><p>" + content + "<br><span>" + messageDate + "</span></p></div></div>");
        }else {
            $("#messages").prepend("<div class='message frnd_message'><div class='user_profile'><img class='room_cover' src='/member/findProfileImg?memberId="+sender+"'><span>" + senderName + "</span></div><p>" + content + "<br><span>" + messageDate + "</span></p></div>");
        }
    }else if(messageType == "ENTER") {
        $("#messages").prepend("<div class='message1 join'><p>" + content + "</p></div>");
        $("#clubMember").text(myName + "님 외 " + (memberCount - 1) + "명");
        $("#clubMan").text(nowMan + " / " + maxMan);
        $(".room_sidvar").append("<div class='block44' id='member_"+sender+"'><div class='list_imgbx'><img src='/member/findProfileImg?memberId="+sender+"' class='user_self'></div><div class='list_details'><div class='listHead'><h4>"+senderName+"</h4></div></div></div>");
    }else if(messageType == "EXIT") {
        $("#messages").prepend("<div class='message1 join'><p>" + content + "</p></div>");
        $("#clubMember").text(myName + "님 외 " + (memberCount - 1) + "명");
        $("#clubMan").text(nowMan + " / " + maxMan);
        $("#member_"+sender).remove();
    }else if(messageType == "EXPIRE") {
        $("#messages").prepend("<div class='message1 join'><p>" + content + "</p></div>");
        $("#message").attr("disabled", true);
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