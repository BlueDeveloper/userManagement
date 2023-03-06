package user.mngm.usermanagement.jpa.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.entity.UserRepository;

/* 유저 서비스 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


    /**
     * @type : Spring Security
     * @desc :
     * UserDetailsService의 필수 메소드로 구현한 로그인 서비스
     * 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
     * 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 UserEntity.java로 반환 타입 지정 (자동으로 다운 캐스팅됨)
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
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
//        return new ResponseEntity<>(message, HttpStatus.OK);
        return null;
    }


}
