package com.xing.fileserver.common.web;

import com.xing.fileserver.common.annotation.IgnoreCommonResponse;
import com.xing.fileserver.common.model.ResultBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Component
// 扫描自定义类，避免与Swagger等冲突
@RestControllerAdvice(basePackages = {"com.xing"})
public class CommonResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        boolean support = !(methodParameter.getMethod().isAnnotationPresent(IgnoreCommonResponse.class) || methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreCommonResponse.class));
        return support;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return ResultBean.success(o);
    }
}
