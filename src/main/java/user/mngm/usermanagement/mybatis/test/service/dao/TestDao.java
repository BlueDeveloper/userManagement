package user.mngm.usermanagement.mybatis.test.service.dao;

import org.springframework.stereotype.Repository;

import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

@Repository
public interface TestDao {

    int userSignUp(UserVo vo);

}
