package user.mngm.usermanagement.common.utils.redis;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 레디스 저장
     * @param key
     * @param value
     * @param path
     * @return
     */
    public boolean set(String key, String value, RedisPathEnum path) {
        boolean result = false;
        try {
            ValueOperations<String, String> redis = redisTemplate.opsForValue();
            String pathKey = path.name + key;
            redis.set(pathKey, value);
            switch (path) {
                case WEB_TOKEN:
                    redisTemplate.expire(pathKey, 10, TimeUnit.HOURS);
                    break;
                case WEB_EMAIL_CERT:
                    if (value.equals("OK")) {
                        redisTemplate.expire(pathKey, 30, TimeUnit.MINUTES);
                    } else {
                        redisTemplate.expire(pathKey, 3, TimeUnit.MINUTES);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("redis set : {}", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 레디스 값구하기
     * @param key
     * @param value
     * @param path
     * @return
     */
    public String get(String id, RedisPathEnum path) {
        String value = null;
        try {
            value = redisTemplate.opsForValue().get(path.name + id);
        } catch (Exception e) {
            log.error("redis get : {}", e);
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * 레디스 조회
     * @param key
     * @param value
     * @param path
     * @return
     */
    public boolean exists(String key, RedisPathEnum path) {
        return redisTemplate.hasKey(path.name + key).booleanValue();
    }

    /**
     * 레디스 삭제
     * @param key
     * @param path
     * @return
     */
    public boolean delete(String key, RedisPathEnum path) {
        return redisTemplate.opsForValue().getOperations().delete(path.name + key);
    }

}