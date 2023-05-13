package user.mngm.usermanagement.jpa.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.common.response.CodeEnum;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.user.dto.UserSearchDto;
import user.mngm.usermanagement.jpa.user.entity.UserEntity;
import user.mngm.usermanagement.jpa.user.repository.UserRepository;


/* 유저 서비스 */
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    public ResponseEntity<ApiResponseEntity> userList(Pageable pageable, UserDto userDto){
        Page<UserSearchDto> page =  userRepository.findBySearchOption(pageable,userDto.getMemberId(),userDto.getName(),userDto.getEmail());
        Long totalCount = page.getTotalElements();
        int countList = page.getSize();
        Long totalPage = totalCount/countList;
        if(totalCount % countList > 0){
            totalPage ++;
        }


        return ApiResponseEntity.setResponse(page, CodeEnum.SUCCESS, "", HttpStatus.OK);
    }
}

