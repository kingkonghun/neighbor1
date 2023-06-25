$(function(){
    $(".open-search").click(function(){
        $("#searchForm_2").css("top", "0px");
    });
    $("html").click(function(e) {
        if ($(e.target).parents("#searchForm_2").length < 1 && e.target.id !== "searchForm_2" && $(e.target).parents(".open-search").length < 1) {
            $("#searchForm_2").css("top", "-64px");
        }
    });
    $("#open-popup").click(function(){
        var _width = 400;
        var _height = 600;
        var dualScreenLeft = window.screenLeft !== undefined ? window.screenLeft : window.screen.left;
        var dualScreenTop = window.screenTop !== undefined ? window.screenTop : window.screen.top;
        var screenWidth = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : window.screen.width;
        var screenHeight = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : window.screen.height;
        var _left = ((screenWidth / 2) - (_width / 2)) + dualScreenLeft;
        var _top = ((screenHeight / 2) - (_height / 2)) + dualScreenTop;
        window.open('/chatRoomList', 'popup-test', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
    });

    $("#btnChat").click(function(){
        var _width = 400;
        var _height = 600;
        var dualScreenLeft = window.screenLeft !== undefined ? window.screenLeft : window.screen.left;
        var dualScreenTop = window.screenTop !== undefined ? window.screenTop : window.screen.top;
        var screenWidth = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : window.screen.width;
        var screenHeight = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : window.screen.height;
        var _left = ((screenWidth / 2) - (_width / 2)) + dualScreenLeft;
        var _top = ((screenHeight / 2) - (_height / 2)) + dualScreenTop;
        window.open('', 'detailPopup', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );

        document.chatForm.action="/openRoom";
        document.chatForm.method="post";
        document.chatForm.target="detailPopup";
        document.chatForm.submit();
    });
});



