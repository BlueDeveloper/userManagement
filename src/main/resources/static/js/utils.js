/**********************************************
 * 모달 종류의 창이 포커싱 되어야 할 때 사용
 * @param flag1 - true=포커싱 : false=포커싱해제
 *********************************************/
function lockBackGround(flag1) {
    if (flag1) {
        const w = $("body").css("width")
        const h = $("body").css("height")
        $("#backGroundMaks").css({width: w, height: h}).toggleClass("d-none")
    } else {
        $("#backGroundMaks").css({width: "0", height: "0"}).toggleClass("d-none")
        $("#loadingIcon").addClass("d-none")
    }
}

/**********************************************
 * URL에 있는 Parameter 가져올 때 사용
 * @param key - 가져올 파라미터 명
 *********************************************/
function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}

/**********************************************
 * 서버에서 응답해준 Pageable 객체를 통해 페이지 번호가 들어있는 html 코드를 반환
 * @param page - 서버에서 응답해준 pageable 값
 *********************************************/
function calPageNum(page){
    console.log(page)
    let pageObject = {pageNumber : "", pageSize : "", totalPages : "", startPage : "", endPage : ""}


}