package user.mngm.usermanagement.mybatis.test.service;

import user.mngm.usermanagement.mybatis.test.service.vo.ResponseVo;
import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

public interface TestService {

    ResponseVo userSignUp (UserVo vo);

    ResponseVo userLogin(UserVo vo);

}
