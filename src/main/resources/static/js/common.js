/** 로그인체크(return true, false) */
function checkIsAuthenticated() { // 권한체크
    let isAuthenticated;
    $.ajax({
        type: "get",
        url: "/member/isAuthenticated",
        async: false,
        success: function (result) {
        isAuthenticated = result;
        }
    });
    return isAuthenticated;
}