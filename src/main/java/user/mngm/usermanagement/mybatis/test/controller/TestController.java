package user.mngm.usermanagement.mybatis.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import user.mngm.usermanagement.mybatis.test.service.TestService;
import user.mngm.usermanagement.mybatis.test.service.vo.ResponseVo;
import user.mngm.usermanagement.mybatis.test.service.vo.UserVo;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/select-test")
    public String select_test() {
        String result = "";
        try {
            // result = testService.SelectTest();
        } catch (Exception e) {
            System.out.println("Exception!!!! : " + e);
        }
        return result;
    }

    @PostMapping("/api/view/user/signUp")
    public ResponseVo userSignUp(UserVo vo, ResponseVo resVo) {
        int result = testService.userSignUp(vo);
        if(result > 0) {
            resVo.setStatus(200);
            resVo.setSuccess(true);
            resVo.setMessage("회원가입에 성공하였습니다.");
        } else {
            resVo.setStatus(400);
            resVo.setSuccess(false);
            resVo.setMessage("회원가입에 실패하였습니다.");
        }
        return resVo;
    }

}
