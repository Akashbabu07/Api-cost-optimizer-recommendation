package com.example.recommendation.cache;

import com.example.recommendation.dto.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationCacheService {

    private final RedisTemplate<String, RecommendationResponse> redisTemplate;

    @Value("${cache.recommendation.ttl:5}")
    private long ttlMinutes;

    private static final String PREFIX = "recommendation:";

    // =========================
    // PUT CACHE
    // =========================
    public void put(String key, RecommendationResponse value) {

        try {
            redisTemplate.opsForValue().set(
                    buildKey(key),
                    value,
                    Duration.ofMinutes(ttlMinutes)
            );

            log.debug("Cache PUT success key={}", key);

        } catch (Exception e) {
            log.error("Cache PUT failed key={}", key, e);
        }
    }

    // =========================
    // GET CACHE (FULLY TYPE SAFE)
    // =========================
    public RecommendationResponse get(String key) {

        try {
            return redisTemplate.opsForValue().get(buildKey(key));

        } catch (Exception e) {
            log.error("Cache GET failed key={}", key, e);
            return null;
        }
    }

    // =========================
    // EVICT CACHE
    // =========================
    public void evict(String key) {

        try {
            redisTemplate.delete(buildKey(key));
            log.debug("Cache EVICT key={}", key);

        } catch (Exception e) {
            log.error("Cache EVICT failed key={}", key, e);
        }
    }

    // =========================
    // EXISTS CHECK
    // =========================
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(key)));
    }

    // =========================
    // INTERNAL KEY BUILDER
    // =========================
    private String buildKey(String key) {
        return PREFIX + key;
    }
}