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
     * <p>주소의 1번째 자리는 Service : api / Front : front 로 분류</p><br>
     * 주소의 2번째 자리는 user, view, admin 세가지로 분류<br>
     * (기본) {?}/user/* --> 로그인이 필요함<br>
     * (기본) {?}/view/* --> 자유롭게 이용 가능<br>
     * (중요) {?}/admin/* --> 로그인한 아이디의 auth == ROLE_ADMIN 이어야 함(db에서 따로 설정 회원가입 시 DEFAULT = USER)
     * <p><br>
     * 3,4 번째 자리는 대분류 소분류로 나눠서 작성
     * 소분류 필요없을 시 대분류만 작성
     * <p><br>
     * 대분류 => notice, map 등 --> 추가 시 기재바람<br>
     * 소분류 => list(자료가 여러건), page(자료가 단일건), detail(단일 자료를 상세히 보는 경우) --> 추가 시 기재바람<br>
     * 추후 문서로 관리 정리 예정
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html"); // /에 해당하는 요청은 main.html 로 보내버림
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE); // 우선순위 제일 높게 설정

        //{권한없는 페이지 접근}
        registry.addViewController("/front/view/access-denied").setViewName("common/access/denied");//{유저_로그인}

        //{유저}
        registry.addViewController("/front/view/user/signIn").setViewName("page/user/signIn");//{유저_로그인}
        registry.addViewController("/front/view/user/signUp").setViewName("page/user/signUp");//{유저_회원가입}
        registry.addViewController("/front/user/myPage").setViewName("page/user/myPage");//{유저_마이페이지}

        //{관리자}
        registry.addViewController("/front/admin/myPage").setViewName("page/admin/adminPage");//{관리자_페이지}

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
