package user.mngm.usermanagement.jpa.user.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.utils.SendMail;
import user.mngm.usermanagement.common.utils.Utils;
import user.mngm.usermanagement.common.utils.redis.RedisPathEnum;
import user.mngm.usermanagement.common.utils.redis.RedisUtil;
import user.mngm.usermanagement.jpa.user.dto.AuthDto;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.entity.UserRepository;

/* 유저 서비스 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final SendMail sendMail;      //메일전송 Service
    private final RedisUtil redisUtil;    //Redis
    private final UserRepository userRepository;
    private final Utils utils;

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
     * @desc : 사용자가 입력한 이메일에 인증번호 전송 후 성공여부를 response해줌
     */
    public ResponseEntity<ApiResponseEntity> sendAuth(AuthDto authDto) {
        try {
            String userEmail = Utils.toStr(authDto.getEmail());
            // 이메일 필수값 체크
            if (StringUtils.isEmpty(userEmail)) {
                return ApiResponseEntity.setResponse(null, "400", "이메일을 입력해주세요", HttpStatus.BAD_REQUEST);
            }

            if(!userEmail.equalsIgnoreCase("a")) {
                String authNum = sendMail.sendSimpleMessage(userEmail); //메일전송
                // 메일전송 실패시 return
                if (StringUtils.isEmpty(authNum)) {
                    return ApiResponseEntity.setResponse(null, "400", "인증번호 전송 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.BAD_REQUEST);
                } 

                redisUtil.set(userEmail, authNum, RedisPathEnum.WEB_EMAIL_CERT); // Redis에 (KEY:메일 주소, Value:전송된 인증번호) 등록 

            } else {
                redisUtil.set("a", "123", RedisPathEnum.WEB_EMAIL_CERT); // Redis에 테스트용 데이터 등록
            }
            return ApiResponseEntity.setResponse(null, "200", "인증번호가 전송되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return ApiResponseEntity.setResponse(null, "404", "인증번호 전송 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.NOT_FOUND, e);
        }
    }

    /**
     * @type : User Management
     * @desc : 사용자가 입력한 인증번호를 value로 redis조회 후 데이터가 있으면 value를 'SUCESS'로 변경
     */
    public ResponseEntity<ApiResponseEntity> findAuth(AuthDto authDto) {
        try {
            // Redis에 저장된 인증번호 조회
            String result = redisUtil.get(authDto.getEmail(), RedisPathEnum.WEB_EMAIL_CERT);

            // 인증정보가 없으면
            if (StringUtils.isEmpty(result) && result.equals(authDto.getAuth())) {
                return ApiResponseEntity.setResponse(null, "204", "잘못된 인증번호 입니다.", HttpStatus.NO_CONTENT);
            }

            redisUtil.set(authDto.getEmail(), "SUCCESS", RedisPathEnum.WEB_EMAIL_CERT); // 인증번호가 일치하면 value="SUCCESS"로 변경

            if (StringUtils.isEmpty(authDto.getFindUserInfo()) || authDto.getFindUserInfo().equalsIgnoreCase("PWD")) {   // 회원가입인지 아이디/비밀번호찾기인지 확인
                // 회원가입과 비밀번호찾기는 보안을 위해 성공여부만 전송
                return ApiResponseEntity.setResponse(null, "200", "인증되었습니다.", HttpStatus.OK);
            } else {
                // 회원정보 조회
                UserEntity userInfo = userRepository.findByEmail(authDto.getEmail());
                // 등록된 회원인지 확인
                if(ObjectUtils.isEmpty(userInfo)) {
                    return ApiResponseEntity.setResponse(null, "204", "등록되지않은 사용자 입니다.", HttpStatus.NO_CONTENT);
                }
                String memberId = userInfo.getMemberId();
                memberId = utils.Masking(memberId);     // 마스킹처리

                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("memberId", memberId);
                
                // 아이디찾기는 마스킹된 아이디 전송
                return ApiResponseEntity.setResponse(resultMap, "200", "인증되었습니다.", HttpStatus.OK);
            }   
        } catch (Exception e) {
            return ApiResponseEntity.setResponse(null, "404", "인증번호 인증 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.NOT_FOUND, e);
        }
    }

    /**
     * @type : User Management
     * @desc : 회원가입 서비스
     */
    public ResponseEntity<ApiResponseEntity> signUp(UserDto userDto) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            if(userDto.getMemberId() == null) {
                return ApiResponseEntity.setResponse(null, "400", "아이디는 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }
            if(userDto.getPwd() == null) {
                return ApiResponseEntity.setResponse(null, "400", "비밀번호는 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }
            if(userDto.getEmail() == null) {
                return ApiResponseEntity.setResponse(null, "400", "이메일은 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }
            if(userDto.getName() == null) {
                return ApiResponseEntity.setResponse(null, "400", "이름은 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }

            String result = redisUtil.get(userDto.getEmail(), RedisPathEnum.WEB_EMAIL_CERT);        // Redis에 인증정보가 SUCCESS인지 조회
            if(StringUtils.isEmpty(result) && result.equals("SUCCESS")) {
                return ApiResponseEntity.setResponse(null, "400", "이메일 인증을 진행해주세요.", HttpStatus.BAD_REQUEST);
            }

            redisUtil.delete(userDto.getEmail(), RedisPathEnum.WEB_EMAIL_CERT);
            
            userDto.setPwd(encoder.encode(userDto.getPwd()));       // 비밀번호 인코딩

            UserEntity user = new UserEntity();
            user.setName(userDto.getName());
            user.setMemberId(userDto.getMemberId());
            user.setPwd(userDto.getPwd());
            user.setEmail(userDto.getEmail());
            user.setStat("01");

            UserEntity signUpResult = userRepository.save(user);

            if(ObjectUtils.isEmpty(signUpResult)) {
                return ApiResponseEntity.setResponse(null, "400", "회원가입 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.BAD_REQUEST);
            }

            return ApiResponseEntity.setResponse(null, "200", "회원가입 성공!.", HttpStatus.CREATED);
        } catch (Exception e) {
            return ApiResponseEntity.setResponse(null, "404", "회원가입 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.NOT_FOUND, e);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponseEntity> password_change(UserDto userDto) {
        UserEntity userInfo = userRepository.findByEmail(userDto.getEmail()); 
        userInfo.pwdUpdate(userDto.getPwd());
        
        ApiResponseEntity response = new ApiResponseEntity(null, "200", "ok");
        return new ResponseEntity<ApiResponseEntity>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponseEntity> jpa_test(String name) {
        UserEntity user = new UserEntity();
        ApiResponseEntity response = new ApiResponseEntity(user, "200", "ok");
        return new ResponseEntity<ApiResponseEntity>(response, HttpStatus.OK);
    }

}
