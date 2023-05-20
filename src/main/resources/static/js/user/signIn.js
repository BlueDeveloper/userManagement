const gbl = {timer: ""}

$(function () {
    init()                                                          // 초기화 함수
    $(".loginBtn").on("click", login)                               // 로그인
    $(".findIdBtn, .findPwBtn").on("click", showFindIdPw)           // 아이디/비밀번호 찾기 버튼 클릭
    $(".sendAuthNumberBtn.id").on("click", findId);                 // 아이디 찾기의 인증번호 찾기
    $(".sendAuthNumberBtn.pw").on("click", findPw);                 // 아이디 찾기의 인증번호 찾기
    $(".checkAuthNumberBtn").on("click", beforeCheckAuthNum);       // 인증번호 확인 버튼
    $(".backArrow").on("click", backArrow);                         // 뒤로가기 버튼 클릭
    $("#pwd").on("keydown", enterLogin);                            // 비밀번호 엔터 로그인
})

function init() {
    $("#memberId").focus()
}

function login() {
    $(".loginFrm").submit()
}

function showFindIdPw(e) {
    $(`.loginTitle`).html(e.target.textContent).css("font-size", "1.4rem")
    $(`.loginFrm, .findIdPwDiv, .loginBtnDiv`).addClass("d-none");
    $(`.backArrow, .${e.target.className.substring(0, 6)}Div`).removeClass("d-none") //아이디/비밀번호 찾기 div + 뒤로가기 화살표 활성화
}

function findId(e) {
    let obj = {
        node: {timeOutMsg: $(".timeOut.id")},  // 인증번호 유효시간 표기 텍스트 노드
        data: {
            email: $(".fEmail.id").val(),  // 이메일 값
            authType: "I"                 // 인증타입 구분
        }
    };
    if (obj.data.email === "" || obj.data.email === undefined || !reg_email.test(obj.data.email)) {
        alert("이메일 형식을 확인해주세요.");
        return false;
    }

    sendAuthNum(obj, () => {
        $(".authBtn.id").toggleClass("d-none")
        $(".inputInfo.id").toggleClass("d-none")
    }, 180);
}

function findPw(e) {
    let obj = {
        node: {
            timeOutMsg: $(".timeOut.pw")
        },  // 인증번호 유효시간 표기 텍스트 노드
        data: {
            email: $(".fEmail.pw").val(),  // 이메일 값
            id: $(".fId.pw").val(),        // 아이디 값
            authType: "P"                 // 인증타입 구분
        }
    };
    if (obj.data.email === "" || !reg_email.test(obj.data.email)) {
        alert("이메일 형식을 확인해주세요.");
        return false;
    }
    if (obj.data.id === "") {
        alert("아이디를 입력해주세요");
        return false;
    }
    sendAuthNum(obj, () => {
        $(".authBtn.pw").toggleClass("d-none")
        $(".inputInfo.pw").toggleClass("d-none")
    }, 180);
}


// 인증번호 확인
function beforeCheckAuthNum(e) {
    const authType = e.target.dataset.authtype;
    if (authType === "I") {
        checkAuthNum(authType, {email: $(".fEmail.id").val(), auth: $(".authNum.id").val(), authType: authType},
            (data) => {
                $(".inputInfo.id").addClass("d-none")
                $(".success.id, .successId").removeClass("d-none").val(data.data);
                clearInterval(gbl.timer);
            })
    } else if (authType === "P") {
        checkAuthNum(authType, {
                email: $(".fEmail.pw").val(),
                id: $(".fId.pw").val(),
                auth: $(".authNum.pw").val(),
                authType: authType
            },
            (data) => {
                // 비밀번호 변경 프로세스 추가
            })
    }
}

function backArrow() {
    $(`.loginTitle`).html(`로그인`).css("font-size", "1.5rem")                      //Title 변경
    $(`.findIdDiv, .findPwDiv, .backArrow`).addClass("d-none")                      //아이디/비밀번호 찾기 div + 뒤로가기 화살표 숨김
    $(`.loginFrm, .findIdPwDiv, .loginBtnDiv`).removeClass("d-none");               //로그인 화면 SHOW
    $(".inputInfo, .sendAuthNumberBtn").removeClass("d-none")                       //아이디/비밀번호에서 보여질 폼
    $(".inputAuth, .checkAuthNumberBtn, .success").addClass("d-none")               //아이디/비밀번호에서 숨겨질 품
    $(".timeOut, .fEmail, .fId, .authNum, .successId").val("").html("");            //인증 유효시간, 이메일, 아이디, 인증번호
    clearInterval(gbl.timer)                                                        //타임아웃 Interval 초기화
}

function enterLogin(e) {
    if (e.keyCode === 13) login()
}