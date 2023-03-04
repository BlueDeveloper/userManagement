package user.mngm.usermanagement.mybatis.test.service.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

@Repository("TestDao")
public class TestDao {

    @Resource
    private SqlSessionTemplate sqlSession;

    public int userSignUp(UserVo vo) {
        return sqlSession.insert("TEST.userSignUp", vo);
    }

    public String userLogin(UserVo vo) {
        return sqlSession.selectOne("TEST.userLogin", vo);
    }

}
