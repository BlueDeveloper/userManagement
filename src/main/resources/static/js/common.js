var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;     // 이메일 형식체크

/**********************************************
 * 공통으로 사용하는 AJAX 함수
 * @param {string} type     - ajax 호출 type  ex) GET, POST, PUT, DELETE
 * @param url               - ajax 호출 url
 * @param data              - ajax param
 * @param asyncType         - true : 비동기 / false : 동기
 * @param beforeSend        - ajax beforeSend 수행 함수
 * @param success           - ajax success 수행 함수
 * @param error             - ajax error 수행 함수

 *********************************************/
function ajaxCall(type, url, data, asyncType, beforeSend, success, error) {
    /*if(tradition == true) {
        $.ajaxSettings.traditional = true;
        tradition=false;
    }*/
    $.ajaxSettings.traditional = true;
    $.ajax({
        type: type,
        url: url,
        data: data,
        async: asyncType,
        beforeSend: beforeSend,
        dataType: "json",
        success: (res) => {
            success(res)
        },
        error: (res) => {
            const data = res.responseJSON // 서버 응답 메시지
            if (typeof error === "function") {
                error(data) // error 함수 설정 시
            } else {
                if (data !== undefined) {
                    alert(data.msg) // error 함수 미설정 시 && 서버 응답 메시지 있을 시
                } else {
                    alert("오류가 발생했습니다."); // error 함수 미설정 시 && 서버 응답 메시지 없을 시
                }
            }
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
 * 이메일 인증번호 전송에 사용
 * @param obj - obj.node 시간초 표기에 사용될 노드
 * @param callBack - 인증번호 전송 완료 후 실행할 함수
 *********************************************/
function sendAuthNum(obj, callBack) {
    ajaxCall("POST", "/api/view/user/sendAuth", obj.data, true, null,
        () => {
            timeout(obj.node.timeOutMsg, 180);
            setTimeout(() => callBack(), 1000);
        }, null)
}


/**********************************************
 * 이메일 인증번호 확인에 사용
 * @param authType - 인증구분 값(J=회원가입, I=아이디찾기, P=비밀번호찾기)
 * @param data - 인증에 필요한 데이터
 * @param callBack - 인증번호 전송 완료 후 실행할 함수
 *********************************************/
function checkAuthNum(authType, data, callBack) {
    ajaxCall("POST", "/api/view/user/find", data, true, null,
        (data) => {
            if (typeof callBack === "function") {
                callBack(data);
            } else {
                alert("인증되었습니다.");
            }
        }, (data) => {
            alert(data.msg); // 인증실패 메세지
            if (data.code === "400") { // 명백한 오류 일 시 화면 return
                clearInterval(gbl.timer);
                $(".backArrow").click();
            }
        })
}


/**********************************************
 * 모달 종류의 창이 포커싱 되어야 할 때 사용
 * @param flag - true=포커싱 : false=포커싱해제
 *********************************************/
function lockBackGround(flag) {
    if (flag) {
        const w = window.innerWidth
        const h = window.innerHeight
        $("#backGroundMaks").css({width: w, height: h}).toggleClass("d-none")
    } else {
        $("#backGroundMaks").css({width: "0", height: "0"}).toggleClass("d-none")
    }
}
