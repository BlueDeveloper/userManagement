package user.mngm.usermanagement.mybatis.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import user.mngm.usermanagement.mybatis.test.service.TestService;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/select-test")
    public String select_test() {
        String result = "";
        try {
            result = testService.SelectTest();
        } catch (Exception e) {
            System.out.println("Exception!!!! : " + e);
        }
        return result;
    }

}
