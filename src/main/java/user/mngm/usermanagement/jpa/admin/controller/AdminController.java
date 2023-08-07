package user.mngm.usermanagement.jpa.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.mngm.usermanagement.common.response.ApiResponseEntity;
import user.mngm.usermanagement.jpa.admin.dto.AccessLogSearchDto;
import user.mngm.usermanagement.jpa.user.dto.UserDto;
import user.mngm.usermanagement.jpa.admin.service.AdminService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    // [관리자-회원관리] 회원목록조회
    @PostMapping("/admin/userList")
    public ResponseEntity<ApiResponseEntity> userList(Pageable pageable, UserDto userDto) {
        return adminService.userList(pageable, userDto);
    }

    // [관리자-회원관리] 회원상태변경
    @PostMapping("/admin/changeUserStat")
    public ResponseEntity<ApiResponseEntity> changeUserStat(Pageable pageable, UserDto userDto) {
        return adminService.changeUserStat(userDto);
    }

    // [관리자-접속기록] 접속기록목록조회
    @PostMapping("/admin/accessLogList")
    public ResponseEntity<ApiResponseEntity> accessLogList(Pageable pageable, AccessLogSearchDto accessLogDto, HttpServletRequest request){
        return adminService.accessLogList(pageable,accessLogDto,request);
    }
}
