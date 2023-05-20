const gbl = {timer: "", ori: ""}
$(function () {
    init()                                                          // 초기화
    $(".changeEmail").on("click", changeEmail)                      // 이메일 변경
    $(".cancleChange").on("click", cancleChange)                    // 이메일 변경 취소
    $("#email").on("keyup", emailKeyDown)                           // 이메일 값 변경 체크
    $(".authEmail").on("click", authEmail)                          // 인증번호 전송(모달창 show)
    $(".checkAuthNumberBtn").on("click", beforeCheckAuthNum);       // 인증번호 확인 버튼
    $(".checkCancel").on("click", checkCancel);                     // 인증번호 취소 버튼
    $(".beforeChgPw").on("click", beforeChgPw)                      // 비밀번호 변경 모달
    $(".changePwBtn").on("click", changePwBtn)                      // 비밀번호 변경 모달
    $(".chagnePwCancel").on("click", chagnePwCancel)                // 비밀번호 변경 취소
})

function init() {
    ajaxCall("POST", "/api/user/myPageInfo", null, true, null,
        (data) => {
            gbl.ori = data.data
            let infoText = $(".infoText")
            $.each(infoText, (idx, target) => $(target).val(gbl.ori[target.id]))
        }, null)
}

function changeEmail(e) {
    $(".emailBtn").toggleClass("d-none")
    $("#email").removeAttr("disabled", false).css({border: "1px solid black"});
}

function cancleChange() {
    $(".emailBtn").toggleClass("d-none")
    $(".authEmail").addClass("d-none");
    $("#email").attr("disabled", true).css({border: "unset"}).val(gbl.ori.email);
}

function emailKeyDown(e) {
    const oriEmail = gbl.ori.email;
    const curEmail = e.target.value;
    if (oriEmail !== curEmail) {
        $(".authEmail").removeClass("d-none");
    } else {
        $(".authEmail").addClass("d-none");
    }
}

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
        const h = window.scrollY + 200;
        $(".authModal").removeClass("d-none").css("top", h);
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
            $(".authModal").addClass("d-none");                                     // 모달창 숨기기
            $("#loadingIcon").removeClass("d-none").css("top", window.scrollY + 200); // 로딩이미지
            setTimeout(() => emailUpdate(), 1000) // 로딩아이콘을 보여주고싶은 개발자의 뒤틀린 욕망을 위한 코드
        })
}

function checkCancel() {
    clearInterval(gbl.timer);
    $(".authModal").toggleClass("d-none");
    $("#auth").val("")
    lockBackGround(false);
    cancleChange()
}

function emailUpdate() {
    ajaxCall("POST", "/api/user/emailUpdate", {email: $("#email").val(), memberId: gbl.ori.memberId}, false, null,
        (data) => {
            location.reload()
        }, null)
}

function beforeChgPw() {
    const h = window.scrollY;
    $(".chgPwModal").removeClass("d-none").css("top", h)
    lockBackGround(true);
}

function changePwBtn() {
    const o = $("#op").val();
    const n1 = $("#np1").val();
    const n2 = $("#np2").val();
    if (o === "" || o === undefined) {
        alert("기존 비밀번호 항목을 입력해주세요");
    } else if (n1 === "" || o === undefined) {
        alert("신규 비밀번호 항목을 입력해주세요");
    } else if (n2 === "" || o === undefined) {
        alert("비밀번호 확인 항목을 입력해주세요");
    } else if (n1 !== n2) {
        alert("비밀번호가 일치하지 않습니다.")
    } else {
        ajaxCall("POST", "/api/user/passwordUpdate", {memberId: gbl.ori.memberId, pwd: o, pwd2: n1}, true, null,
            (data) => {
                alert(data.msg)
                location.href = "/api/user/signOut";
            }, null)
    }
}

function chagnePwCancel() {
    $(".chgPwModal").addClass("d-none")
    lockBackGround(false);
    $(".pwInp").val("");
}