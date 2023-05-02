package user.mngm.usermanagement.jpa.user.dto;

import lombok.Data;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;

@Data
public class UserDto {
    private String name;
    private String memberId;
    private String pwd;
    private String pwd2;
    private String email;

    public UserDto(UserEntity user) {
        this.name = user.getName();
        this.memberId = user.getMemberId();
        this.email = user.getEmail();
    }

    public UserDto(){

    }
}
