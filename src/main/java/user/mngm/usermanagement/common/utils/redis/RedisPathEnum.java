package user.mngm.usermanagement.common.utils.redis;

public enum RedisPathEnum {

    WEB_TOKEN("web:token:"), // WEB_TOKEN
    WEB_EMAIL_CERT("web:email:cert:"); // WEB_EMAIL_CERT

   String name;

    RedisPathEnum(String name) {
        this.name = name;
    } 
}
