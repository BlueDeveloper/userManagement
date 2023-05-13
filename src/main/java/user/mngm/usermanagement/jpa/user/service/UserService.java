package user.mngm.usermanagement.jpa.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.response.CodeEnum;
import user.mngm.usermanagement.common.utils.SendMail;
import user.mngm.usermanagement.common.utils.Utils;
import user.mngm.usermanagement.common.utils.redis.RedisPathEnum;
import user.mngm.usermanagement.common.utils.redis.RedisUtil;
import user.mngm.usermanagement.jpa.user.dto.AuthDto;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.user.entity.QUserEntity;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.repository.UserRepository;

import java.util.Optional;

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
            // 인증 구분값 미전송 시
            if (authDto.getAuthType() == null) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "비정상적인 접근입니다.", HttpStatus.BAD_REQUEST);
            }

            String userEmail = Utils.toStr(authDto.getEmail());
            // 이메일 필수값 체크
            if (StringUtils.isEmpty(userEmail)) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
            }

            // 회원가입 시
            if (authDto.getAuthType().equalsIgnoreCase("J")) {
                Optional<UserEntity> userEntity = userRepository.findByEmail(authDto.getEmail());
                if (userEntity.isPresent()) return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "이미 등록된 이메일 입니다.", HttpStatus.BAD_REQUEST);
            }

            // 비밀번호 찾기 일 경우 ID로 사용자 조회
            if (authDto.getAuthType().equalsIgnoreCase("P")) {
                Optional<UserEntity> userEntity = userRepository.findByMemberId(authDto.getId());
                if (!userEntity.isPresent()) return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "등록되지않은 사용자 입니다.", HttpStatus.BAD_REQUEST);
            }

            String authNum = sendMail.sendSimpleMessage(userEmail); //메일전송
            // 메일전송 실패시 return
            if (StringUtils.isEmpty(authNum)) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "인증번호 전송 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.BAD_REQUEST);
            }

            redisUtil.set(userEmail, authNum, RedisPathEnum.WEB_EMAIL_CERT); // Redis에 (KEY:메일 주소, Value:전송된 인증번호) 등록

            /*if (!userEmail.equalsIgnoreCase("a")) {
                String authNum = sendMail.sendSimpleMessage(userEmail); //메일전송
                // 메일전송 실패시 return
                if (StringUtils.isEmpty(authNum)) {
                    return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "인증번호 전송 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.BAD_REQUEST);
                }

                redisUtil.set(userEmail, authNum, RedisPathEnum.WEB_EMAIL_CERT); // Redis에 (KEY:메일 주소, Value:전송된 인증번호) 등록
            } else {
                redisUtil.set("a", "123", RedisPathEnum.WEB_EMAIL_CERT); // Redis에 테스트용 데이터 등록
            }*/
            return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "인증번호가 전송되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "인증번호 전송 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.NOT_FOUND, e);
        }
    }

    /**
     * @type : User Management
     * @desc : 사용자가 입력한 인증번호를 value로 redis조회 후 데이터가 있으면 value를 'SUCESS'로 변경
     */
    public ResponseEntity<ApiResponseEntity> findAuth(AuthDto authDto) {
        try {
            /*
             * 1. redis에 authDto.getEmail()과 일치하는 정보가 있는지 체크
             * 2. redis에 저장된 인증번호와 authDto.getAuth()이 일치하는지 체크
             * 3. 위 두가지 조건 통과 시 redis정보 SUCCESS로 업데이트
             * 4. 회원가입 경우 - 인증되었습니다. 문구 응답
             * 5. 아이디찾기 경우 - authDto.getEmail()의 값으로 사용자 조회 후 데이터 유무 에 따른 응답
             * 6. 비밀번호찾기 경우 -authDto.getId()의 값으로 사용자 조회 후 데이터 유무에 따른 응답
             * ps1. authDto.getAuthType()값이 없을 경우 최하단 return 실행
             * ps2. J = 회원가입, I = 아이디찾기, P = 비밀번호찾기
             * */
            String result = redisUtil.get(authDto.getEmail(), RedisPathEnum.WEB_EMAIL_CERT); // Redis에 저장된 인증번호 조회

            if (StringUtils.isEmpty(result)) { // Redis에 저장된 인증정보가 없을 시
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "인증정보가 없습니다.", HttpStatus.BAD_REQUEST);
            }

            if (!result.equals(authDto.getAuth())) { // 사용자가 입력한 정보와 다를시
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "잘못된 인증번호 입니다.", HttpStatus.BAD_REQUEST);
            }

            redisUtil.set(authDto.getEmail(), "SUCCESS", RedisPathEnum.WEB_EMAIL_CERT); // 인증번호가 일치하면 value="SUCCESS"로 변경

            if (authDto.getAuthType().equalsIgnoreCase("J")) { // 회원가입
                return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "인증되었습니다.", HttpStatus.OK); // 보안을 위해 성공여부만 전송
            }

            if (authDto.getAuthType().equalsIgnoreCase("I")) { // 아이디찾기
                Optional<UserEntity> userEntity = userRepository.findByEmail(authDto.getEmail()); // 정보조회

                if (ObjectUtils.isEmpty(userEntity)) { // 등록된 회원인지 확인
                    return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "등록되지않은 사용자 입니다.", HttpStatus.BAD_REQUEST);
                }

                String memberId = utils.Masking(userEntity.get().getMemberId()); // 아이디찾기는 마스킹된 아이디 전송
                return ApiResponseEntity.setResponse(memberId, CodeEnum.SUCCESS, "인증되었습니다.", HttpStatus.OK);
            }


            if (authDto.getAuthType().equalsIgnoreCase("P")) { // 비밀번호 찾기
                Optional<UserEntity> userEntity = userRepository.findByMemberId(authDto.getId());
                if (!userEntity.isPresent()) return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "등록되지않은 사용자 입니다.", HttpStatus.BAD_REQUEST);
                return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "인증되었습니다.", HttpStatus.OK); // 보안을 위해 성공여부만 전송
            }


            // 인증 구분값 미전송 시
            return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "비정상적인 접근입니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "인증번호 인증 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.BAD_REQUEST, e);
        }
    }

    /**
     * @type : User Management
     * @desc : 회원가입 서비스
     */
    public ResponseEntity<ApiResponseEntity> signUp(UserDto userDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            if (userDto.getMemberId() == null) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "아이디는 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }
            if (userDto.getPwd() == null) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "비밀번호는 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }
            if (userDto.getEmail() == null) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "이메일은 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }
            if (userDto.getName() == null) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "이름은 필수값 입니다.", HttpStatus.BAD_REQUEST);
            }

            String result = redisUtil.get(userDto.getEmail(), RedisPathEnum.WEB_EMAIL_CERT);        // Redis에 인증정보가 SUCCESS인지 조회
            if (StringUtils.isEmpty(result) && result.equals("SUCCESS")) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "이메일 인증을 진행해주세요.", HttpStatus.BAD_REQUEST);
            }

            redisUtil.delete(userDto.getEmail(), RedisPathEnum.WEB_EMAIL_CERT); // 이전 인증 내역 삭제

            // TODO ModelMapper() 사용 검토
            UserEntity user = new UserEntity();
            user.setName(userDto.getName());
            user.setMemberId(userDto.getMemberId());
            user.setPwd(encoder.encode(userDto.getPwd())); // 비밀번호 인코딩
            user.setEmail(userDto.getEmail());
            user.setStat("01");

            UserEntity signUpResult = userRepository.save(user);

            if (ObjectUtils.isEmpty(signUpResult)) {
                return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "회원가입 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.BAD_REQUEST);
            }

            return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "회원가입 성공\r\n환영합니다. ", HttpStatus.CREATED);
        } catch (Exception e) {
            return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "회원가입 실패\r\n관리자에게 문의바랍니다.(010-2480-7840)", HttpStatus.NOT_FOUND, e);
        }
    }

    /**
     * @type : User Management
     * @desc : 마이페이지 서비스
     */
    public ResponseEntity<ApiResponseEntity> myPageInfo(UserEntity principal) {
        UserDto userDto = new UserDto(principal);
        return ApiResponseEntity.setResponse(userDto, CodeEnum.SUCCESS, "", HttpStatus.OK);
    }

    /**
     * @type : User Management
     * @desc : 이메일 변경 서비스
     */
    @Transactional
    public ResponseEntity<ApiResponseEntity> emailUpdate(UserDto userDto) {
        Optional<UserEntity> userEntity = userRepository.findByMemberId(userDto.getMemberId());
        if (!userEntity.isPresent()) return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "등록되지않은 사용자 입니다.", HttpStatus.BAD_REQUEST);
        userEntity.get().setEmail(userDto.getEmail()); // JPA 변경감지(Dirty Checking)

        // 업데이트된 정보를  Spring Security로 인증해서 HttpSession에 반영
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(authentication, userDto.getMemberId()));

        return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "", HttpStatus.OK);
    }

    /**
     * @type : User Management
     * @desc : 비밀번호 변경 서비스
     */
    @Transactional
    public ResponseEntity<ApiResponseEntity> passwordUpdate(UserDto userDto) {
        Optional<UserEntity> userEntity = userRepository.findByMemberId(userDto.getMemberId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(userDto.getPwd(), userEntity.get().getPwd())) {
            return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "기존 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        userEntity.get().pwdUpdate(encoder.encode(userDto.getPwd2())); // 신규 비밀번호 암호화 후 JPA 변경감지(Dirty Checking)
        return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "변경되었습니다.\r\n재로그인 해주시길 바랍니다.", HttpStatus.OK);
    }

    /**
     * @param currentAuth 현재 auth 정보
     * @param memberId    현재 사용자 Id
     * @return Authentication
     * @description 새로운 인증 생성
     * @author Armton
     */
    protected Authentication createNewAuthentication(Authentication currentAuth, String memberId) {
        UserDetails newPrincipal = userRepository.findByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException((memberId))); //memberId를 기반으로 유저 조회
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
        newAuth.setDetails(currentAuth.getDetails());
        return newAuth;
    }




    /*public ResponseEntity<ApiResponseEntity> jpa_test(String name) {
        UserEntity userEntity = new UserEntity();
        ApiResponseEntity response = new ApiResponseEntity(userEntity, CodeEnum.SUCCESS, "ok");
        return new ResponseEntity<ApiResponseEntity>(response, HttpStatus.OK);
    }*/
}
