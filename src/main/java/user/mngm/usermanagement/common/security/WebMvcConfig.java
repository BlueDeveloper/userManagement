package user.mngm.usermanagement.common.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/* mvc 관련 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/", "classpath:/public/", "classpath:/", "classpath:/resources/", "classpath:/META-INF/resources/"};


    /**
     * 주소의 기본적인 형태
     * {대분류}/{중분류}/{소분류}
     * --------------------------------------------------------------------------------------------------------------
     * 주소의 1번째 ---> 대분류
     * (서비스) api/...
     * (페이지) front/...
     * --------------------------------------------------------------------------------------------------------------
     * 주소의 2번째 ---> 중분류 [user, view, admin] 세가지로 분류
     * (기본) {대분류}/user/... --> 로그인이 필요함
     * (기본) {대분류}/view/... --> 자유롭게 이용 가능
     * (중요) {대분류}/admin/... --> 로그인한 아이디의 auth == ROLE_ADMIN 이어야 함(db에서 따로 설정 회원가입 시 DEFAULT = USER)
     * --------------------------------------------------------------------------------------------------------------
     * 주소의 3번째 ---> 소분류 [알맞은 단어] 로 분류
     * (예시) 다수의 정보(list), 단일 정보(page), 정보의 상세(detail) 등
     * (예시) {대분류}/{중분류}/signIn
     * (예시) {대분류}/{중분류}/list
     * (예시) {대분류}/{중분류}/page
     * --------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html"); // /에 해당하는 요청은 main.html 로 보내버림
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE); // 우선순위 제일 높게 설정

        //{권한없는 페이지 접근}
        registry.addViewController("/front/view/access-denied").setViewName("/common/access/denied");//{유저_로그인}

        //{유저}
        registry.addViewController("/front/view/user/signIn").setViewName("/page/user/signIn");//{유저_로그인}
        registry.addViewController("/front/view/user/signUp").setViewName("/page/user/signUp");//{유저_회원가입}
        registry.addViewController("/front/user/myPage").setViewName("/page/user/myPage");//{유저_마이페이지}

        //{관리자}
        registry.addViewController("/front/admin/myPage").setViewName("/page/admin/adminPage");//{관리자_페이지}

//        //{공지사항}
//        registry.addViewController("/api/view/notice/list").setViewName("/page/notice/notice_list");//{공지사항_리스트}
//        registry.addViewController("/api/view/notice/detail").setViewName("/page/notice/notice_detail");//{공지사항_상세보기}
//        registry.addViewController("/api/admin/notice/insert").setViewName("/page/notice/notice_insert");//{공지사항_수정하기}
//        //{오시는길}
//        registry.addViewController("/api/view/item/items").setViewName("/page/items/items.html");//{오시는 길}
//        //{예약하기}
//        registry.addViewController("/api/view/reservation/list").setViewName("/page/reservation/reservation_list");//{예약하기_리스트}
    }

    // 정적 자원 핸들
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}
