package user.mngm.usermanagement.jpa.user.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String email;
    private String id;
    private String auth;
    private String authType; // I = 아이디찾기, P = 비밀번호 찾기
}
