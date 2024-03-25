package com.wwwhynot3.manager.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huanglb.common.AuthTokenPool;
import com.huanglb.common.Authorization.AuthorizationInterceptor;
import com.huanglb.common.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class BeanFactory {
    @Bean
    public JsonUtil jsonUtil() {
        return new JsonUtil(new ObjectMapper());
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
}
