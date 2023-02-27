package user.mngm.usermanagement.test.service.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("TestDao")
public class TestDao {

    @Resource
    private SqlSessionTemplate sqlSession;

    public String SelectTest() {
        return sqlSession.selectOne("USER.selectTest");
    }

}
