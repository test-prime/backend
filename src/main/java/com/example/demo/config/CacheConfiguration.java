package com.example.demo.config;

import io.lettuce.core.dynamic.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
//    @Bean
//    public RedissonClient redissonClient() throws IOException {
//        Config config = Config.fromYAML(new ClassPathResource("redisson-jcache.yaml").getInputStream());
//        config.setCodec(new org.redisson.codec.SerializationCodec());
//        return Redisson.create(config);
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedissonClient redissonClient) {
//        return new RedissonSpringCacheManager(redissonClient);
//    }
}