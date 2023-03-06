package user.mngm.usermanagement.mybatis.test.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import user.mngm.usermanagement.mybatis.test.service.TestService;
import user.mngm.usermanagement.mybatis.test.service.dao.TestDao;
import user.mngm.usermanagement.mybatis.test.service.vo.ResponseVo;
import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

@Service("TestService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    /*
     * 일반유저 회원가입
     */
    @Override
    public ResponseVo userSignUp(UserVo vo) {

        ResponseVo resVo = new ResponseVo();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            if(vo.getMember_id() == null) {
                resVo.setStatus(400);
                resVo.setSuccess(false);
                resVo.setMessage("아이디 필수값 누락");
            }
            if(vo.getPwd() == null) {
                resVo.setStatus(400);
                resVo.setSuccess(false);
                resVo.setMessage("패스워드 필수값 누락");
            }
            if(vo.getEmail() == null) {
                resVo.setStatus(400);
                resVo.setSuccess(false);
                resVo.setMessage("이메일 필수값 누락");
            }
            if(vo.getName() == null) {
                resVo.setStatus(400);
                resVo.setSuccess(false);
                resVo.setMessage("이름 필수값 누락");
            }
            vo.setPwd(encoder.encode(vo.getPwd()));

            testDao.userSignUp(vo);

        } catch (Exception e) {
            resVo.setStatus(400);
            resVo.setSuccess(false);
            resVo.setMessage("API 예외처리 발생");
            return resVo;
        }
        return resVo;
    }

    @Override
    public ResponseVo userLogin(UserVo vo) {
        ResponseVo resVo = new ResponseVo();
        try {
            if(ObjectUtils.isEmpty(vo.getMember_id())) {
                resVo.setStatus(400);
                resVo.setSuccess(false);
                resVo.setMessage("아이디 필수값 누락");
            }
            if(ObjectUtils.isEmpty(vo.getPwd())) {
                resVo.setStatus(400);
                resVo.setSuccess(false);
                resVo.setMessage("패스워드 필수값 누락");
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            vo.setPwd(encoder.encode(vo.getPwd()));

            testDao.userLogin(vo);

        } catch (Exception e) {
            resVo.setStatus(400);
            resVo.setSuccess(false);
            resVo.setMessage("API 예외처리 발생");
            return resVo;
        }
        return resVo;
    }

}
