package com.uk.bootintegrationall.springmvc.config;

import com.uk.bootintegrationall.springmvc.exception.ClientException;
import com.uk.bootintegrationall.springmvc.exception.ClientExceptionEnum;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Description TODO
 */
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var token = webRequest.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            var jwt = token.substring(7);
            var userInfo = new UserInfo();
            userInfo.setId(1L);
            userInfo.setName("admin");
            userInfo.setRole("admin");
            return userInfo;
        }else {
            throw new ClientException(ClientExceptionEnum.UNAUTHORIZED);
        }
    }
}
