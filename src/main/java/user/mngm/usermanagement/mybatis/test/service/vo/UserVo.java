package user.mngm.usermanagement.mybatis.test.service.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("UserVo")
public class UserVo {

    private String member_id;

    private String pwd;

    private String email;

    private String name;

}
