package user.mngm.usermanagement.mybatis.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.mngm.usermanagement.mybatis.test.service.TestService;
import user.mngm.usermanagement.mybatis.test.service.dao.TestDao;
import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

@Service("TestService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    /*
     * 일반유저 회원가입
     */
    @Override
    public int userSignUp(UserVo vo) {
        try {
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
    
}
