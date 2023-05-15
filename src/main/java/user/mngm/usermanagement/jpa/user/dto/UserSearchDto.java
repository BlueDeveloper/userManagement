package user.mngm.usermanagement.jpa.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.sql.Date;

@Data
public class UserSearchDto {
    private String memberId;
    private String name;
    private String email;
    private String stat;
    private Date loginDat;

    @QueryProjection
    public UserSearchDto(String memberId, String name, String email, String stat, Date loginDat){
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.stat = stat;
        this.loginDat = loginDat;
    }
}
