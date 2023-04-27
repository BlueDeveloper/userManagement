package user.mngm.usermanagement.common.response;

public enum CodeEnum {
    SUCCESS("200"), // 요청 수행 성공
    WAITING("300"), // 요청 수행 실패 - 이후 로직 수행 대기
    FAIL("400");    // 요청 수행 실패

    String code;

    CodeEnum(String code) {
        this.code = code;
    }
}
