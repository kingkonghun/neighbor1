$(function(){
    $(".open-search").click(function(){
        $("#searchForm_2").css("top", "0px");
    });
    $("html").click(function(e) {
        if ($(e.target).parents("#searchForm_2").length < 1 && e.target.id !== "searchForm_2" && $(e.target).parents(".open-search").length < 1) {
            $("#searchForm_2").css("top", "-64px");
        }
    });
});


