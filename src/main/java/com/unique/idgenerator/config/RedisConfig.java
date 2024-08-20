package com.unique.idgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.unique.idgenerator.model.TrackingNumber;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, TrackingNumber> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, TrackingNumber> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        
        // Set serializers
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        return template;
    }
}
