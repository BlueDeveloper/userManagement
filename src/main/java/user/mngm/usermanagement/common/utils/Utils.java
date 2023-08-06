package user.mngm.usermanagement.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

@Component
public class Utils {

    // object가 문자열인지 판독
    public static String toStr(Object o) {
        if(o == null){
            return "";
        }else{
            return o.toString();
        }
    }

    // 절반 마스킹처리
    public String Masking(String str) {
        if(StringUtils.isEmpty(str)) {
            return "";
        } else {
            int strLength = (str.length() / 2);
            String maskingStr = str.replaceAll("(?<=.{" + strLength + "}).", "*");
            return maskingStr;
        }
    }

    public static Date getSqlDateTime(){
        return new java.sql.Date(new java.util.Date().getTime());
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
