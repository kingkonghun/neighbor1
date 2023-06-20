/** 로그인체크(return true, false) */
function checkIsAuthenticated() {
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

/** 게스트면 true */
function checkIsAuthorization() {
    let isAuthorization;
    $.ajax({
        type: "get",
        url: "/member/isAuthorization",
        async: false,
        success: function (result) {
        isAuthorization = result;
        }
    });
    return isAuthorization;
}