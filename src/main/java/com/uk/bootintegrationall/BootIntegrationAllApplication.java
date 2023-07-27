package com.uk.bootintegrationall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
public class BootIntegrationAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootIntegrationAllApplication.class, args);
    }

}
