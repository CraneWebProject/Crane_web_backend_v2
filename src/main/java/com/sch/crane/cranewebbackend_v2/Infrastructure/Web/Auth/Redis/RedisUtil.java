package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Transactional
public class RedisUtil {
    private final StringRedisTemplate template;

    public String getData(String key){
        ValueOperations<String , String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public void setValues(String token, String email){
        ValueOperations<String, String> values = template.opsForValue();
        values.set(token, email);
        template.expire(token, Duration.ofMinutes(3)); // 3분 후에 만료
    }

    public boolean existData(String key){
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String  value, long duration){
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        template.delete(key);
    }
}

