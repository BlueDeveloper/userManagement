package user.mngm.usermanagement.jpa.user.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String email;
    private String id;
    private String auth;
    private String findUserInfo;
}
