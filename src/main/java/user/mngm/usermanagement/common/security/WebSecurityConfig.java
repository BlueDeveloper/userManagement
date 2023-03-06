package user.mngm.usermanagement.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import user.mngm.usermanagement.common.exception.AjaxAuthenticationEntryPoint;
import user.mngm.usermanagement.jpa.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Security 설정 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // 로그인 로직이 실행될 서비스
    // 해당 서비스(userService)에서는 UserDetailsService를 implements해서 loadUserByUsername() 구현해야함 (서비스 참고)
    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/view/*", "/api/view/*/*", "/api/send/*", "/api/send/*/*","/front/view/*", "/front/view/*/*").permitAll()     // 해당 url 무조건 허용
                .antMatchers("/api/admin/*", "/api/admin/*/*", "/front/admin/*", "/front/admin/*/*").hasRole("ADMIN")                           // 해당 url ADMIN 권한 필요
                .antMatchers("/api/user/*", "/api/user/*/*", "/front/user/*", "/front/user/*/*").authenticated()                                // 해당 url 인증 필요
                .anyRequest().permitAll()                                                                                                                          // 나머지 요청은 모두 허용
                .and()                                                  // --------------------------------------------------------------------------------------------------------------------------------------------
                .formLogin()                                            // 로그인 설정
                .loginPage("/front/view/user/signIn")                   // 로그인 페이지
                .loginProcessingUrl("/api/view/user/signIn")            // 로그인 액션(Default => /login)
                .usernameParameter("memberId")                          // 아이디 파라미터명 설정
                .passwordParameter("pwd")                               // 패스워드 파라미터명 설정
                .successHandler(new loginSuccess())                     // 성공 시 수행될 로직
                .failureHandler(new loginFailure())                     // 실패 시 수행될 로직
                .and()                                                  // --------------------------------------------------------------------------------------------------------------------------------------------
                .logout()                                               // 로그아웃 설정
                .logoutSuccessUrl("/")                                  // 로그아웃 시 이동할 페이지
                .invalidateHttpSession(true)                            // 로그아웃시 세션소멸
                .and()                                                  // --------------------------------------------------------------------------------------------------------------------------------------------
                .exceptionHandling().authenticationEntryPoint(new AjaxAuthenticationEntryPoint("/front/view/access-denied"));// 미인증 사용자가 인증 필요 url 접속시 핸들링

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // UserDetailsService를 구현받아 loadUserByUsername를 구현한 클래스를 인자값으로 넣고 암호화 방식을 지정함
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    private class loginSuccess implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            System.out.println("authentication : " + authentication.getPrincipal());
            response.sendRedirect("/"); // 인증이 성공한 후에는 root로 이동
        }
    }

    private class loginFailure implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            System.out.println("exception : " + exception.toString());
            response.sendRedirect("/front/view/user/signIn?fail"); // 인증이 실패하면 로그인 화면 유지
        }
    }
}
