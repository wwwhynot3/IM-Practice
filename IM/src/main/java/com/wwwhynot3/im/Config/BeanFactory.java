package com.wwwhynot3.im.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huanglb.common.AuthTokenPool;
import com.huanglb.common.Authorization.AuthorizationInterceptor;
import com.huanglb.common.JsonUtil;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import com.huanglb.common.SnowFlakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class BeanFactory {
    @Value("${im.id-generator.data-center-id}")
    private Long dataCenterId;
    @Value("${im.id-generator.machine-id}")
    private Long machineId;

    @Bean
    @Autowired
    public JsonUtil jsonUtil(ObjectMapper objectMapper) {
        return new JsonUtil(objectMapper);
    }

    @Bean
    @Autowired
    public AuthTokenPool authTokenPool(StringRedisTemplate stringRedisTemplate) {
        return new AuthTokenPool(stringRedisTemplate);
    }

    @Bean
    @Autowired
    public AuthorizationInterceptor authorizationInterceptor(AuthTokenPool authTokenPool, JsonUtil jsonUtil) {
        return new AuthorizationInterceptor(authTokenPool, jsonUtil);
    }

    @Bean
    @Autowired
    public ImMessageService imMessageService(MongoTemplate mongoTemplate) {
        return new ImMessageService(mongoTemplate);
    }

    @Bean
    public SnowFlakeIdGenerator snowFlakeIdGenerator() {
        return new SnowFlakeIdGenerator(dataCenterId, machineId);
    }
}

