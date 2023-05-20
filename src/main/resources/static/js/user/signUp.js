const gbl = {timer: "", mailAuthYn: false}
$(function () {
    $("#sendAuth").on("click", authEmail)                           // 인증하기 버튼
    $(".checkAuthNumberBtn").on("click", beforeCheckAuthNum);       // 인증번호 확인 버튼
    $(".checkCancel").on("click", checkCancel)                      // 인증창 닫기
    $(".joinBtn").on("click", join)                                 // 회원가입
    $(".cancleBtn").on("click", signUpCancle)                       // 회원가입 취소
})

function authEmail() {
    let obj = {
        node: {timeOutMsg: $(".timeOut")},  // 인증번호 유효시간 표기 텍스트 노드
        data: {email: $("#email").val(), authType: "J"}
    }
    if (obj.data.email === "" || !reg_email.test(obj.data.email)) {
        alert("이메일 형식을 확인해주세요.");
        return false;
    }
    sendAuthNum(obj, () => {
        lockBackGround(true);
    }, 180);
}

function beforeCheckAuthNum() {
    const auth = $("#auth").val()
    const authType = "J"
    if (auth === "") {       // 인증번호 체크
        alert("인증번호를 입력해주세요.");
        return $(auth).focus();
    }
    checkAuthNum(authType, {auth: auth, email: $("#email").val(), authType: authType},
        (data) => {
            alert(data.msg);
            $(".authModal, #sendAuth").addClass("d-none");
            $("#email").attr("disabled", true);
            gbl.mailAuthYn = true;
            lockBackGround(false);
        })
}

function checkCancel() {
    clearInterval(gbl.timer);
    $(".authModal").toggleClass("d-none");
    $("#auth").val("")
    lockBackGround(false);
}

function join() {
    let memberId = $("#memberId").val();
    let pwd = $("#pwd").val();
    let name = $("#name").val();
    let email = $("#email").val();

    if (name === "") {        // 이름 체크
        alert("이름을 입력해주세요.");
        return false;
    }

    if (name.length > 10) {
        alert("이름은 최대 10자까지 입력가능 합니다.");
        return false;
    }

    if (memberId === "") {       // 아이디 체크
        alert("아이디를 입력해주세요.");
        return;
    }
    if (pwd === "") {     // 패스워드 체크
        alert("패스워드를 입력해주세요.");
        return;
    }

    if (!gbl.mailAuthYn) {       // 메일인증여부 체크
        alert("메일인증을 진행해주세요.");
        return;
    }

    if (!reg_email.test(email)) {        // 이메일 형식인지 체크
        alert("이메일 형식이 아닙니다.");
        return false;
    }

    let param = {memberId: memberId, pwd: pwd, name: name, email: email};

    ajaxCall("POST", "/api/view/signUp", param, true, null,
        (data) => {
            alert(data.msg)
            location.href = "/front/view/signIn";
        }, null);
}

function signUpCancle() {
    location.href = "/";
}