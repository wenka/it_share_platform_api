package com.wenka.commons.web;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@ControllerAdvice
public class MResponseAdvice implements ResponseBodyAdvice<Object> {
    public MResponseAdvice() {
    }

    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest)request).getServletRequest();
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse)response).getServletResponse();
        Object o = httpServletRequest.getAttribute("authToken");
        if(o != null) {
            AuthToken token = (AuthToken)o;
            token.setTimeout(token.getTimeout());
            response.getHeaders().add("m-at-id", token.toString());
            httpServletResponse.addCookie(token.toCookie());
        }

        return body;
    }
}
