package user.mngm.usermanagement.common.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* 미인증 요청일 시 핸들링 WebSecurityConfig에서 설정 */
public class AjaxAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public AjaxAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    // 미인증 사용자가 인증 필요 url 접속시 핸들링
    // ajax 요청일 시 ajaxResponse
    // 일반 url 요청일 시 WebMvcConfig.java 의 /api/view/access-denied 주소로 redirect
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String ajaxHeader = request.getHeader("X-Requested-With");

        // AJAX 요청인지 검사 (헤더 검사, 비동기인지 체크)
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);

        // 403 Error를 반환한다.
        if (isAjax) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "허용되지 않는 요청입니다.");
        } else {
            super.commence(request, response, authException);
        }
    }
}
