package user.mngm.usermanagement.jpa.user.entity;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import user.mngm.usermanagement.common.jpa.GenUuid;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_USER")
public class UserEntity extends GenUuid implements UserDetails {
    // 로그인 ID
    @Column(name = "MEMBER_ID")
    private String memberId;

    // 패스워드
    @Column(name = "PWD")
    private String pwd;

    // 이메일
    @Column(name = "EMAIL")
    private String email;

    // 이름
    @Column(name = "NAME")
    private String name;

    // 상태(01:활동, 02: 정지, 03: 탈퇴)
    @Column(name = "STAT")
    private String stat;

    // 가입일자
    @Column(name = "CRT_DAT")
    private String crtDat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // 최종 로그인 시간
    @Column(name = "LOGIN_DAT")
    private String loginDat;

    // 로그인 실패 횟수
    @Column(name = "LOGIN_FAIL")
    private String loginFail;

    // 탈퇴일자
    @Column(name = "OUT_DAT")
    private String outDat;

    // 정보 수정 일자
    @Column(name = "UP_DAT")
    private String upDat;

    //권한
    @Column(name = "GRADE")
    private String grade;

    // 비고
    @Column(name = "BIGO")
    private String bigo;

    @Builder
    public UserEntity(String memberId, String password, String name, String auth, String phone) {
        this.memberId = memberId;
        this.pwd = password;
        this.name = name;
        this.email = email;
        this.stat = stat;
        this.crtDat = crtDat;
        this.loginDat = loginDat;
        this.loginFail = loginFail;
        this.outDat = outDat;
        this.upDat = upDat;
        this.grade = "ROLE_USER";
        this.bigo = bigo;
    }

    public UserEntity() {

    }


    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : grade.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return memberId;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return pwd;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        // true -> 만료되지 않았음
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        // true -> 잠금되지 않았음
        return true;
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        // true -> 만료되지 않았음
        return true;
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        // true -> 사용 가능
        return true;
    }
}
