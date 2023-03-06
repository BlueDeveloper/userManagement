package user.mngm.usermanagement.jpa.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.utils.Utils;
import user.mngm.usermanagement.jpa.user.dto.AuthDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.entity.UserRepository;

import java.util.Map;
import java.util.Optional;

/* 유저 서비스 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     * @type : Spring Security
     * @desc :
     * UserDetailsService의 필수 메소드로 구현한 로그인 서비스
     * 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
     * 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 UserEntity.java로 반환 타입 지정 (자동으로 다운 캐스팅됨)
     */
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return userRepository.findByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException((memberId))); //memberId를 기반으로 유저 조회
    }


    /**
     * @type : User Management
     * @desc : 회원가입 서비스
     */
    public ResponseEntity<?> save(/*UserDto userDto*/) {
        //        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //        userDto.setPassword(encoder.encode(userDto.getPassword()));
        //        userRepository.save(UserEntity.builder()
        //                .email(userDto.getEmail())
        //                .phone(userDto.getPhone())
        //                .password(userDto.getPassword()).build()).getUuid();
        //        ApiResponseMessage message = new ApiResponseMessage(null, "Ok", "회원가입을 축하드립니다.");
        //        return new ApiResponseEntity<>(message, HttpStatus.OK);
        return null;
    }

    /**
     * @type : User Management
     * @desc : 사용자가 입력한 인증번호에 해당되는 이메일을 맵에서 꺼내와서 해당 값으로 DB조회 후 ID를 response해줌
     */
    public ResponseEntity<ApiResponseEntity> find(AuthDto authDto, Map<Integer, String> authMap) {
        int auth = Integer.parseInt(authDto.getAuth());
        UserEntity user = new UserEntity();
        if (!"".equals(Utils.toStr(authMap.get(auth)))) {
            user = userRepository.findByEmail(authMap.get(auth));
        }

        // 유저정보 없을 시
        if(user == null){
            ApiResponseEntity response = new ApiResponseEntity(null, "204", "일치하는 정보가 없습니다.");
            return new ResponseEntity<ApiResponseEntity>(response, HttpStatus.NO_CONTENT);
        }

        // TODO 유저의 상태에따라 로직분기
        ApiResponseEntity response = new ApiResponseEntity(user.getMemberId(), "200", "");
        return new ResponseEntity<ApiResponseEntity>(response, HttpStatus.OK);
    }
}
