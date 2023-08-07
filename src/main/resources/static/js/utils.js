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
function calPageNum(page, callBack) {
    const pageNumber = page.pageable.pageNumber + 1;
    const pageSize = page.pageable.pageSize;
    const totalPages = page.totalPages;
    const startPage = Math.floor((pageNumber - 1) / pageSize) * pageSize + 1
    const tempEndPage = startPage + pageSize - 1
    const endPage = (tempEndPage > totalPages) ? totalPages : tempEndPage


    let pageNumHtml = ``;
    pageNumHtml += `<span class="first hover num" data-num="1"></span>`
    pageNumHtml += `<span class="prev hover num" data-num="${(!page.first) ? pageNumber - 1 : 1}"></span>`
    pageNumHtml += `<div class="nums noselect">`
    for (let i = startPage; i <= endPage; i++) {
        pageNumHtml += `<span class='num hover ${(pageNumber === i) ? "currentPage" : ""}' data-num="${i}">${i}</span>`
    }
    pageNumHtml += `</div>`
    pageNumHtml += `<span class="next hover num" data-num="${(!page.last) ? pageNumber + 1 : pageNumber}"></span>`
    pageNumHtml += `<span class="last hover num" data-num="${totalPages}"></span>`
    return pageNumHtml
}

/**********************************************
 * 날짜 포맷 yyyyMMddHHmmss -> yyyy/MM/dd HH:mm:ss
 * @param date - 파싱할 날짜 문자열
 *********************************************/
function dateFormat(date) {
    return `${date.substring(0,4)}/${date.substring(4,6)}/${date.substring(6,8)} ${date.substring(8,10)}:${date.substring(10,12)}:${date.substring(12,14)}`
}