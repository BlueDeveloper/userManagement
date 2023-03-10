package user.mngm.usermanagement.common.utils.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@ConfigurationProperties(prefix = "spring.redis")
@Component
@Data
public class RedisProperties {

    private String host;
    private int port;
    private String password;
    
}