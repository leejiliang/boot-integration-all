package com.uk.bootintegrationall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
@EnableRetry
public class BootIntegrationAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootIntegrationAllApplication.class, args);
    }

}
