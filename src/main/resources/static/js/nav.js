$(function(){
    $(".searchIcon").click(function(){
        $(".border").css("display", "none");
        $(".search").css("width", "180px");
        $(".searchSelect").fadeIn();
        $(".searchInput").fadeIn();
        $(".searchInput").focus();
    });
    $('html').click(function(e) {
        if($(e.target).parents('.searchForm').length < 1){
            $(".border").css("display", "block");
            $(".search").css("width", "44px");
            $(".searchInput").css("display", "none");
            $(".searchSelect").css("display", "none");
        }
    });
});
