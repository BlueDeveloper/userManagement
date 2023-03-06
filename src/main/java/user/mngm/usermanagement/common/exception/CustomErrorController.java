package user.mngm.usermanagement.common.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/* 404 혹은 TemplateEngineException -> 경로에 파일 없을 시 발생하는 에러에 대한 후처리 컨트롤러 */
@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView errorHandler(HttpServletRequest req) {
        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // 에러처리 페이지
        String DEFAULT_ERROR_PAGE = "/common/exception/exception";
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_PAGE);
        mav.addObject("code", statusCode.toString());
        return mav;
    }
}
