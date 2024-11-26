package com.example.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

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