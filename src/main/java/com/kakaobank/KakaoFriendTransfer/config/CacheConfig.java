package com.kakaobank.KakaoFriendTransfer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisConfig redisConfig;

    private final static String HashKey = "transferFind";
    private final static String ExpireHashKey = "transferExpire";

    @Bean
    public CacheManager cacheManager() {

        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(defaultRedisCacheConfiguration())
                .withInitialCacheConfigurations(customConfigurationMap())
                .build();

        return redisCacheManager;
    }

    /*
     * JSON Serialization 과 관련된 이슈 존재...
     * https://velog.io/@zooneon/Java-ObjectMapper%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-JSON-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0
     * */
    private RedisCacheConfiguration defaultRedisCacheConfiguration() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(redisConfig.objectMapper())))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()))
//                .entryTtl(Duration.ofHours(24)); // Expire Time
                .entryTtl(Duration.ofMinutes(3));

        return redisCacheConfiguration;
    }

    private Map<String, RedisCacheConfiguration> customConfigurationMap() {
        Map<String, RedisCacheConfiguration> customConfigurationMap = new HashMap<>();

        customConfigurationMap.put(HashKey, defaultRedisCacheConfiguration().entryTtl(Duration.ofMinutes(1)));
        customConfigurationMap.put(ExpireHashKey, defaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds(30)));

        return customConfigurationMap;
    }
}
