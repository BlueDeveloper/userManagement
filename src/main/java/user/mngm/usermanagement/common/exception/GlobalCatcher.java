package user.mngm.usermanagement.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/* exception 처리 */
// 프로젝트 전체 에러 catch
// catch 필요한 에러 하나씩 추가 바람
@ControllerAdvice
public class GlobalCatcher {

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // requestMapping method 불일치 에러
    @ExceptionHandler(Exception.class)
    public String cather(Exception e, Model model) {
        model.addAttribute("ex", e);
        return "common/error/exception"; //에러 return 페이지

    }
}
