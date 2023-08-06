package user.mngm.usermanagement.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import user.mngm.usermanagement.common.utils.Utils;
import user.mngm.usermanagement.jpa.accessLog.entity.AccessLogEntity;
import user.mngm.usermanagement.jpa.accessLog.repository.AccessLogRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class WebInterceptor implements HandlerInterceptor {
    private final AccessLogRepository accessLogRepository;



    /* 클라이언트 요청을 컨트롤러에 전달하기 전에 호출됨, false리턴 시 컨트롤러를 호출하지 않음 */ 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String rUri = request.getRequestURI();
        String rHost = Utils.getClientIP(request);
        String referer = request.getHeader("referer");



        if(referer != null || rUri.equalsIgnoreCase("/")){
            AccessLogEntity accessLogEntity = new AccessLogEntity();
            accessLogEntity.setRUri(rUri);
            accessLogEntity.setRHost(rHost);
            accessLogRepository.save(accessLogEntity);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /* 클라이언트 요청을 처리한 뒤 호출됨 컨트롤러에서 예외 발생 시 실행되지 않음 */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /* 클라이언트 요청을 마치고 클라이언트에서 뷰를 통해 응답을 전송한 뒤 실행됨, 뷰생성 시 예외가 발생해도 실행됨 */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
