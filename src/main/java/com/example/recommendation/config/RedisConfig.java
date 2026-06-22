package com.example.recommendation.config;

import com.example.recommendation.dto.RecommendationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, RecommendationResponse> redisTemplate(
            RedisConnectionFactory factory) {

        RedisTemplate<String, RecommendationResponse> template =
                new RedisTemplate<>();

        template.setConnectionFactory(factory);

        // JSON serializer (safe + standard)
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer();

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());

        // Value serializer (IMPORTANT FIX)
        template.setValueSerializer(serializer);

        // Hash serializers (safe consistency)
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
}