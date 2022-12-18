package org.example.booking.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan("org.example.booking")
public class MyConfig {

    @PostConstruct
    public void init() {
        System.out.println("ПОДНЯЛСЯ КОНФИГ");
    }
}
