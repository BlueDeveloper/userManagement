const gbl = {}
$(function () {
    init()                                                          // 초기화
    $(".searchBtn").on("click", beforeSearchFromBtn)
})

function init() {
    search("/api/admin/userList", "", drowUserList)
    leftMenu();
}

function drowUserList(data) {
    let userHtml = ``
    $.each(data.data.content, (idx, target) => {
        userHtml += `<div class='bodyDiv num${idx}'>
                                            <span>${target.name}</span><span>${target.memberId}</span>
                                            <span>${target.email}</span><span>${target.loginDat}</span>
                                            <span>
                                                <select class="form-select w-u70 userStatSct" data-memberId="${target.memberId}">
                                                    <option value="01" ${(target.stat === "활동") ? "selected" : ""}>활동</option>
                                                    <option value="02" ${(target.stat === "정지") ? "selected" : ""}>정지</option>
                                                    <option value="03" ${(target.stat === "탈퇴") ? "selected" : ""}>탈퇴</option>
                                                </select>
                                            </span>
                                         </div>`
    })
    $(".body").html(userHtml); // 유저 리스트
    $(".footer").html(calPageNum(data.data)) // 페이지 번호 세팅
    $(".num").on("click", beforeSearchFromNums) // 페이지 이벤트 설정
    $(".userStatSct").on("change", chagneUserStat) // 페이지 이벤트 설정
}


function beforeSearchFromBtn(e) {
    let obj = {"page": 0, "size": 5}
    obj[$(".searchSct.defaultSct option:selected").val()] = $(".searchInp").val()
    obj["stat"] = $(".searchSct.statSct option:selected").val()
    search("/api/admin/userList", obj, drowUserList);
}

function beforeSearchFromNums(e) {
    let obj = {"page": Number(e.target.dataset.num) - 1, "size": "5"}
    obj[$(".searchSct option:selected").val()] = $(".searchInp").val()
    obj["stat"] = $(".searchSct.statSct option:selected").val()
    search("/api/admin/userList", obj, drowUserList);
}

function chagneUserStat(e) {
    if (confirm("회원의 상태를 변경하시겠습니까?")) {
        let data = {memberId: e.target.dataset.memberid, stat: e.target.value}
        ajaxCall("POST", "/api/admin/changeUserStat", data, true, null,
            (data) => {
                let obj = {"page": Number($(".currentPage")[0].dataset.num) - 1, "size": "5",}
                obj[$(".searchSct option:selected").val()] = $(".searchInp").val()
                search("/api/admin/userList", obj, drowUserList);
            }, null)
    }
}