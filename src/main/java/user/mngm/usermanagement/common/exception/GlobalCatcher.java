package user.mngm.usermanagement.common.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateEngineException;

/* exception 처리 */
// 프로젝트 전체 에러 catch
// catch 필요한 에러 하나씩 추가 바람
// 에러에 대한 각각의 화면처리가 필요하면 return 화면 변경 바람
@ControllerAdvice
public class GlobalCatcher {
    private final String DEFAULT_EXCEPTION_PAGE = "/common/exception/catcher"; // 기본 예외처리 페이지

    // RequestMappingMethod 불일치 핸들러
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(Exception.class)
    public String handleMethodNotAllowed(Exception e, Model model) {
        model.addAttribute("ex", e);
        return DEFAULT_EXCEPTION_PAGE; //에러 return 페이지
    }

    // 데이터베이스에러 핸들러
    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(DataAccessException e) {
        e.printStackTrace();
        return DEFAULT_EXCEPTION_PAGE;
    }
}
