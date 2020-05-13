package cn.tyson.simplefilemanager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class LockUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Boolean tryLock(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, 60, TimeUnit.SECONDS);
    }

    public Boolean unlock(String key, String value) {
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
            return stringRedisTemplate.delete(key);
        }
        return false;
    }

    public Boolean relock(String key, String value) {
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
            return stringRedisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }
        return false;
    }
}
