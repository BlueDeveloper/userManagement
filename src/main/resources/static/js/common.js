var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;     // 이메일 형식체크

/**********************************************
 * 공통으로 사용하는 AJAX 함수
 * @param {string} type     - ajax 호출 type  ex) GET, POST, PUT, DELETE
 * @param url               - ajax 호출 url
 * @param data              - ajax param
 * @param beforeSend        - ajax beforeSend 수행 함수
 * @param success           - ajax success 수행 함수
 * @param error             - ajax error 수행 함수

 *********************************************/
function ajaxCall(type, url, data, beforeSend, success, error) {
    /*if(tradition == true) {
        $.ajaxSettings.traditional = true;
        tradition=false;
    }*/
    $.ajaxSettings.traditional = true;
    $.ajax({
        type: type,
        url: url,
        data: data,
        dataType: "json",
        beforeSend: beforeSend,
        success: (res) => {
            success(res)
        },
        error: (res) => {
            error(res.responseJSON)
        }
    });
}

//ajaxCall() // FIXME 이거 왜 자동 실행해둔거임?? 좆상민씨 해명하세요

/**********************************************
 * data가 배열일 경우 사용하는 AJAX 함수
 * Controller에서 @RequestBody String으로 받고
 * 파싱해서 사용 가능
 * @param url               - ajax 호출 url
 * @param data              - ajax param
 * @param beforeSend        - ajax beforeSend 수행 함수
 * @param success           - ajax success 수행 함수
 * @param error             - ajax error 수행 함수
 *********************************************/

function ajaxCallList(url, data, beforeSend, success, error) {
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        beforeSend: beforeSend,
        success: success,
        error: error
    });
}

// TODO 파일업로드 기능 추가바람
function ajaxFormData(url, data, beforeSend, success, error) {
    $.ajaxSettings.traditional = true;
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        beforeSend: beforeSend,
        success: success,
        error: error
    });
}


/**********************************************
 * 모달 종류의 창이 포커싱 되어야 할 때 사용
 * @param flag - true=포커싱 : false=포커싱해제
 *********************************************/
function lockBackGround(flag) {
    if(flag){
        const w = window.innerWidth
        const h = window.innerHeight
        $("#backGroundMaks").css({width: w, height: h}).toggleClass("d-none")
    }else{
        $("#backGroundMaks").css({width: "0", height: "0"}).toggleClass("d-none")
    }

}
