package user.mngm.usermanagement.mybatis.test.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import user.mngm.usermanagement.common.utils.SendMail;
import user.mngm.usermanagement.common.utils.redis.RedisPathEnum;
import user.mngm.usermanagement.common.utils.redis.RedisUtil;
import user.mngm.usermanagement.mybatis.test.service.TestService;
import user.mngm.usermanagement.mybatis.test.service.vo.ResponseVo;
import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private SendMail sendMail;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/select-test")
    public String select_test() throws UnsupportedEncodingException, MessagingException {
        String result = "aoaao78@naver.com";
        String code = "";
        try {
            code = sendMail.sendSimpleMessage(result, "테스트");
            // result = testService.SelectTest();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return code;
    }

    @PostMapping("/api/view/user/signUp")
    public ResponseVo userSignUp(UserVo vo, ResponseVo resVo) {

        ResponseVo result = testService.userSignUp(vo);

        return result;
    }

    @PostMapping("/api/view/user/login")
    public ResponseVo userLogin(UserVo vo, ResponseVo resVo) {

        ResponseVo result = testService.userLogin(vo);

        return result;
    }

    @PostMapping("/redis-test")
    public String redis_test() {
        String mail = "123@naver.com";
        String key = "abc123";
        redisUtil.set(key, mail, RedisPathEnum.WEB_EMAIL_CERT);     // Redis에 인증번호와 메일주소 등록
        String value = redisUtil.get(key, RedisPathEnum.WEB_EMAIL_CERT);        // 인증번호로 Redis에서 메일주소 조회
        redisUtil.set(mail, "OK", RedisPathEnum.WEB_EMAIL_CERT);
        System.out.println("value : " + value);

        return value;
    }

}
