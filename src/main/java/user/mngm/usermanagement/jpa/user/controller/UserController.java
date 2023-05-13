package user.mngm.usermanagement.jpa.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.response.CodeEnum;
import user.mngm.usermanagement.jpa.user.dto.AuthDto;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.service.AdminService;
import user.mngm.usermanagement.jpa.user.service.UserService;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private AdminService adminService;

    // [유저] 이메일 인증번호 전송
    @PostMapping("/view/sendAuth")
    public ResponseEntity<ApiResponseEntity> sendAuth(AuthDto authDto) {return userService.sendAuth(authDto);}

    // [유저] 인증번호 검증 후 Redis에 Sucess으로 값 변경
    @PostMapping("/view/find")
    public ResponseEntity<ApiResponseEntity> findAuth(AuthDto authDto) {return userService.findAuth(authDto);}

    // [유저] 회원가입
    @PostMapping("/view/signUp")
    public ResponseEntity<ApiResponseEntity> signUp(UserDto userDto) {return userService.signUp(userDto);}

    // [유저] 마이페이지 내 정보
    @PostMapping("/user/myPageInfo")
    public ResponseEntity<ApiResponseEntity> myPageInfo(@AuthenticationPrincipal UserEntity principal) {return userService.myPageInfo(principal);}

    // [유저] 비밀번호 변경
    @PostMapping("/user/passwordUpdate")
    public ResponseEntity<ApiResponseEntity> passwordUpdate(UserDto userDto) {return userService.passwordUpdate(userDto);}

    // [유저] 이메일 변경
    @PostMapping("/user/emailUpdate")
    public ResponseEntity<ApiResponseEntity> emailUpdate(UserDto userDto) {return userService.emailUpdate(userDto);}

    // [관리자] 유저관리를 위한 유저 목록
    @PostMapping("/admin/userList")
    public ResponseEntity<ApiResponseEntity> userList(Pageable pageable, UserDto userDto) {return adminService.userList(pageable,userDto);}
}
