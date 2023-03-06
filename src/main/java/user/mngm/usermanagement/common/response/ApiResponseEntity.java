package user.mngm.usermanagement.common.response;

import lombok.Data;

@Data
public class ApiResponseEntity {
    private Object data;
    private String msg;
    private String code;


    public ApiResponseEntity() {
    }

    public ApiResponseEntity(Object data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

}
