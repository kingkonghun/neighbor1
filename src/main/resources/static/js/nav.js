$(function(){
    $(".open-search").click(function(){
        $("#searchForm_2").css("top", "0px");
    });
    $("html").click(function(e) {
        if ($(e.target).parents("#searchForm_2").length < 1 && e.target.id !== "searchForm_2" && $(e.target).parents(".open-search").length < 1) {
            $("#searchForm_2").css("top", "-64px");
        }
    });
    // $("#btnSearch_1").click(function(){
    //     var searchForm = $("#searchForm_1");
    //     var searchSelect = $(".searchSelect").val();
    //     if(searchSelect == "used"){
    //         searchForm.attr('action', '/used/list');
    //     }else if(searchOption == "club"){
    //         searchForm.action = "#";
    //     }
    //     searchForm.submit();
    // });
    // $("#btnSearch_2").click(function(){
    //     $("#searchForm_2").submit();
    // });
    // $('#searchForm_1').keypress(function(event) {
    //     if (event.keyCode === 13) {
    //         event.preventDefault();

    //         var searchForm = $("#searchForm_1");
    //         var searchSelect = $(".searchSelect").val();
    //         if(searchSelect == "used"){
    //             searchForm.attr('action', '/used/list');
    //         }else if(searchOption == "club"){
    //             searchForm.action = "#";
    //         }
    //         searchForm.submit();
    //     }
    // });
    // $('#searchForm_2').keypress(function(event) {
    //     if (event.keyCode === 13) {
    //         event.preventDefault();

    //         var searchForm = $("#searchForm_2");
    //         var searchSelect = $(".searchSelect").val();
    //         if(searchSelect == "used"){
    //             searchForm.attr('action', '/used/list');
    //         }else if(searchOption == "club"){
    //             searchForm.action = "#";
    //         }
    //         searchForm.submit();
    //     }
    // });
});



