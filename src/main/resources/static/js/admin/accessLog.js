const gbl = {}
$(function () {
    init()                                                          // 초기화
    $(".searchBtn").on("click", beforeSearchFromBtn)
})

function init() {
    search("/api/admin/accessLogList", "", drowAccLogList)
    leftMenu();
}

function drowAccLogList(data) {
    let logHtml = ``
    $.each(data.data.content, (idx, target) => {
        logHtml += `<div class='bodyDiv num${idx}'>
                                            <span>${target.rhost}</span><span>${target.ruri}</span>
                                            <span>${dateFormat(target.accDat)}</span>
                                         </div>`
    })
    $(".body").html(logHtml); // 유저 리스트
    $(".footer").html(calPageNum(data.data)) // 페이지 번호 세팅
    $(".num").on("click", beforeSearchFromNums) // 페이지 이벤트 설정
    $(".userStatSct").on("change", chagneUserStat) // 페이지 이벤트 설정
}

function beforeSearchFromBtn(e) {
    let obj = {"page": 0, "size": 5};
    let startDat = $(".searchInp.accDatStart").val();
    let endDat = $(".searchInp.accDatEnd").val();

    obj["rHost"] = $(".searchInp.rHost").val()

    if ((startDat !== "" && endDat === "") || (startDat === "" && endDat !== "")) {
        alert("검색일은 시작일~종료일을 전부 입력해주셔야 합니다.")
        return false;
    }

    if (startDat !== "") {
        if (startDat.length > 0 && startDat.length < 8) {
            alert("검색일이 잘못되었습니다. YYYYMMDD 형식을 지켜주십시오")
            return false;
        } else {
            obj["accDatStart"] = startDat
        }
    }

    if (endDat !== "") {
        if (endDat.length > 0 && endDat.length < 8) {
            alert("검색일이 잘못되었습니다. YYYYMMDD 형식을 지켜주십시오")
            return false;
        } else {
            obj["accDatEnd"] = endDat
        }
    }

    search("/api/admin/accessLogList", obj, drowAccLogList);
}

function beforeSearchFromNums(e) {
    let obj = {"page": Number(e.target.dataset.num) - 1, "size": "5"}
    obj[$(".searchSct option:selected").val()] = $(".searchInp").val()
    obj["stat"] = $(".searchSct.statSct option:selected").val()
    search("/api/admin/accessLogList", obj, drowAccLogList);
}

function chagneUserStat(e) {
    if (confirm("회원의 상태를 변경하시겠습니까?")) {
        let data = {memberId: e.target.dataset.memberid, stat: e.target.value}
        ajaxCall("POST", "/api/admin/changeUserStat", data, true, null,
            (data) => {
                let obj = {"page": Number($(".currentPage")[0].dataset.num) - 1, "size": "5",}
                obj[$(".searchSct option:selected").val()] = $(".searchInp").val()
                search("/api/admin/accessLogList", obj, drowAccLogList);
            }, null)
    }
}