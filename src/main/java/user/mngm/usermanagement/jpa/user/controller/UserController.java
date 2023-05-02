package user.mngm.usermanagement.jpa.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.response.CodeEnum;
import user.mngm.usermanagement.jpa.user.dto.AuthDto;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.service.UserService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    // 로그인은 스프링 시큐리티가 처리했으니 안심하라구!(WebSecurityConfig.java)

    // 이메일 인증번호 전송
    @PostMapping("/view/sendAuth")
    public ResponseEntity<ApiResponseEntity> sendAuth(AuthDto authDto) {
        return userService.sendAuth(authDto);
    }

    // 인증번호 검증 후 Redis에 Sucess으로 값 변경
    @PostMapping("/view/find")
    public ResponseEntity<ApiResponseEntity> findAuth(AuthDto authDto) {
        return userService.findAuth(authDto);
    }

    // 회원가입
    @PostMapping("/view/signUp")
    public ResponseEntity<ApiResponseEntity> signUp(UserDto userDto) {
        return userService.signUp(userDto);
    }

    @PostMapping("/user/myPageInfo")
    public ResponseEntity<ApiResponseEntity> myPageInfo(@AuthenticationPrincipal UserEntity principal) {
        return userService.myPageInfo(principal);
    }

    // 비밀번호 변경
    @PostMapping("/user/passwordUpdate")
    public ResponseEntity<ApiResponseEntity> passwordUpdate(UserDto userDto) {
        return userService.passwordUpdate(userDto);
    }

    // 이메일 변경
    @PostMapping("/user/emailUpdate")
    public ResponseEntity<ApiResponseEntity> emailUpdate(UserDto userDto) {
        return userService.emailUpdate(userDto);
    }

    // 마이페이지 정보
    /*// JPA TEST
    @PostMapping("/view/user/jpa-test")
    public ResponseEntity<ApiResponseEntity> jpa_test(String name) {
        return userService.jpa_test(name);
    }*/

}
