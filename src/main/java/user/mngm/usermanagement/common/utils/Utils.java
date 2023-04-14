package user.mngm.usermanagement.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    public static String toStr(Object o) {
        if(o == null){
            return "";
        }else{
            return o.toString();
        }
    }

    public String Masking(String str) {      // 절반 마스킹처리
        if(StringUtils.isEmpty(str)) {
            return "";
        } else {
            int strLength = (str.length() / 2);
            String maskingStr = str.replaceAll("(?<=.{" + strLength + "}).", "*");
            return maskingStr;
        }
    }
}
