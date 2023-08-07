/**********************************************
 * 관리자 화면 leftmenu active 공통 js
 *********************************************/
function leftMenu() {
    const menu = searchParam("menu");
    $(".leftMenu").removeClass("active");
    (menu === null) ? $(".leftMenu.tab-1").addClass("active") : $(".leftMenu.tab-" + menu).addClass("active");
}

/**********************************************
 * 관리자 화면 검색 공통
 *********************************************/
function search(url, data, callBack) {
    data = (data === "") ? {"page": 0, "size": 5} : data
    ajaxCall("POST", url, data, true, null,
        (data) => {
            callBack(data);
        }, null)
}