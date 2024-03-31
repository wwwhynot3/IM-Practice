package com.huanglb.datatransfer.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huanglb.common.JsonUtil;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class BeanFactory {
    @Bean
    @Autowired
    public JsonUtil jsonUtil(ObjectMapper objectMapper) {
        return new JsonUtil(objectMapper);
    }

    @Bean
    @Autowired
    public ImMessageService messageService(MongoTemplate mongoTemplate) {
        return new ImMessageService(mongoTemplate);
    }


}
