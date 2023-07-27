package com.uk.bootintegrationall.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Description TODO
 */
@Component
public class MyAuditorAware implements AuditorAware<Long> {
    /**
     * 可以从Security, req中获取当前用户信息
     * @return
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(888L);
    }
}
