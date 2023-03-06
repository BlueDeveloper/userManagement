package user.mngm.usermanagement.jpa.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.utils.Utils;
import user.mngm.usermanagement.jpa.user.dto.AuthDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    private static Map<Integer, String> authMap = new HashMap<Integer, String>(); //인증번호 저장장소는 추후 DB 혹은 Redis로 변경


    // 로그인은 스프링 시큐리티가 처리했으니 안심하라구!(WebSecurityConfig.java)

    // 이메일 인증번호 전송(서비스 구현 필요)
    @PostMapping("/view/user/sendAuth")
    public ResponseEntity<ApiResponseEntity> sendAuth(AuthDto authDto) {
        // 이메일 + 인증번호 정보
        String userEmail = Utils.toStr(authDto.getEmail());
        String userId = Utils.toStr(authDto.getId());
        int authNum = ThreadLocalRandom.current().nextInt(100000, 1000000);
        System.out.println("유저 이메일 :: " + userEmail);
        System.out.println("유저 ID :: " + userId);
        System.out.println("인증번호 :: " + authNum);

        authMap.put(authNum, userEmail); // 인증번호 저장 추후 저장장소 변경


        ApiResponseEntity response = new ApiResponseEntity(null, "200", "");
        return new ResponseEntity<ApiResponseEntity>(response, HttpStatus.OK);
    }

    // 인증번호 검증 및 아이디 response
    @PostMapping("/view/user/find")
    public ResponseEntity<ApiResponseEntity> find(AuthDto authDto) {
        return userService.find(authDto, authMap);
    }
}
