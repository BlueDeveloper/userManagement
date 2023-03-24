package user.mngm.usermanagement.jpa.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String memberId;
    private String pwd;
    private String email;
}
