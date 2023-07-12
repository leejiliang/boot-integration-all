package com.uk.bootintegrationall.springmvc.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @Description TODO
 */
@ControllerAdvice
public class FastMvcResponseBodyAwareAdvice implements ResponseBodyAdvice<Object> {

    private final Map<Method, Boolean> supportsCache = new WeakHashMap<>();
    private final String [] basePackages;
    private ObjectMapper objectMapper ;

    public FastMvcResponseBodyAwareAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.basePackages= new String[]{"com.uk.bootintegrationall"};
    }
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (supportsCache.containsKey(returnType.getMethod())) {
            return supportsCache.get(returnType.getMethod());
        }
        boolean isSupport = getIsSupport(returnType);
        supportsCache.put(returnType.getMethod(), isSupport);
        return isSupport;
    }

    private boolean getIsSupport(MethodParameter returnType) {
        Class<?> declaringClass = returnType.getMember().getDeclaringClass();

        IgnoreAware classIgnore = declaringClass.getAnnotation(IgnoreAware.class);
        IgnoreAware methodIgnore = returnType.getMethod().getAnnotation(IgnoreAware.class);
        if (classIgnore != null || methodIgnore != null || returnType.getGenericParameterType().getTypeName().startsWith(CResult.class.getName())) {
            return false;
        }
        for (int i = 0; i < basePackages.length; i++) {
            if (declaringClass.getPackage().getName().startsWith(basePackages[i])) {
                return false;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        CResult<Object> result = CResult.ofSuccess(body);
        if (returnType.getGenericParameterType().equals(String.class)) {
            try {
                response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
