package user.mngm.usermanagement.mybatis.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.mngm.usermanagement.mybatis.test.service.TestService;
import user.mngm.usermanagement.mybatis.test.service.dao.TestDao;

@Service("TestService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public String SelectTest() {
        return testDao.SelectTest();
    }
}
