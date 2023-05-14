package user.mngm.usermanagement.jpa.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserSearchDto {
    private String memberId;
    private String name;
    private String email;
    private String stat;

    @QueryProjection
    public UserSearchDto(String memberId, String name, String email, String stat){
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.stat = stat;
    }
}
