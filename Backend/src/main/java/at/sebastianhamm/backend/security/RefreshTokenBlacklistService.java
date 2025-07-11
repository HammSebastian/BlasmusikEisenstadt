package at.sebastianhamm.backend.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenBlacklistService {

    private static final String PREFIX = "blacklist:refreshToken:";

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expiryMillis) {
        long ttl = expiryMillis - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(PREFIX + token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isBlacklisted(String token) {
        Boolean hasKey = redisTemplate.hasKey(PREFIX + token);
        return hasKey != null && hasKey;
    }
}
