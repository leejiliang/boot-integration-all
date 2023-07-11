package com.uk.bootintegrationall.springmvc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * @Description TODO
 */
@WebFilter(urlPatterns = "/*", initParams = {@WebInitParam(name = "allowedMethods", value = "GET,POST")})
public class LoggerFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("LoggerFilter init...");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("{} before...", servletRequest.getRemoteAddr());
    }

    @Override
    public void destroy() {
        logger.info("LoggerFilter destroy...");
        Filter.super.destroy();
    }
}
