package com.wwwhynot3.manager.Config.MybatisPlus;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MybatisPlusConfig {
    @Bean
    public InsertIgnoreBatchSqlInjector easySqlInjector() {
        return new InsertIgnoreBatchSqlInjector();
    }

    @Bean
    public GlobalConfig globalConfig(@Qualifier("easySqlInjector") ISqlInjector easySqlInjector) {
        System.out.println("------------fuck you----------------");
        GlobalConfig globalConfig = new GlobalConfig();

        System.out.println("------------shit bro, trying to set your customized sql injector----------------");
        System.out.print("--------og sql injector is");
        System.out.print(globalConfig.getSqlInjector().toString());
        globalConfig.setSqlInjector(easySqlInjector);
        System.out.println("after setting is");
        System.out.println(globalConfig.getSqlInjector());
        return globalConfig;
    }
}
