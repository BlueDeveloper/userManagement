package user.mngm.usermanagement.jpa.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.response.CodeEnum;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.user.dto.UserSearchDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.repository.UserRepository;

import java.util.Optional;


/* 유저 서비스 */
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponseEntity> userList(Pageable pageable, UserDto userDto) {
        Page<UserSearchDto> page = userRepository.findBySearchOption(pageable, userDto.getMemberId(), userDto.getName(), userDto.getEmail(), userDto.getStat());
        page.getContent().stream().forEach(e -> {
            if (e.getStat().equalsIgnoreCase("01")) e.setStat("활동");
            if (e.getStat().equalsIgnoreCase("02")) e.setStat("정지");
            if (e.getStat().equalsIgnoreCase("03")) e.setStat("탈퇴");
        });

        return ApiResponseEntity.setResponse(page, CodeEnum.SUCCESS, "", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ApiResponseEntity> changeUserStat(UserDto userDto) {
        Optional<UserEntity> userEntity = userRepository.findByMemberId(userDto.getMemberId());
        if (!userEntity.isPresent()) return ApiResponseEntity.setResponse(null, CodeEnum.FAIL, "등록되지않은 사용자 입니다.", HttpStatus.BAD_REQUEST);
        userEntity.get().setStat(userDto.getStat()); // JPA 변경감지(Dirty Checking)

        return ApiResponseEntity.setResponse(null, CodeEnum.SUCCESS, "", HttpStatus.OK);
    }
}

