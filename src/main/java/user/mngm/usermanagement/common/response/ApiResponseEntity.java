package user.mngm.usermanagement.common.response;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<ApiResponseEntity> setResponse(@Nullable Object data, String code, String message, HttpStatus status){
        return new ResponseEntity<>(new ApiResponseEntity(data, code, message), status);
    }

    public static ResponseEntity<ApiResponseEntity> setResponse(@Nullable Object data, String code, String message, HttpStatus status, Exception e) {
        e.getStackTrace(); // TODO log로 변경하기 e.toString()
        return new ResponseEntity<>(new ApiResponseEntity(data, code, message), status);
    }
}
