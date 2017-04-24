package com.wenka.commons.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class RestExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
    private HttpMessageConverter<?>[] messageConverters = null;
    private List<HttpMessageConverter<?>> allMessageConverters = null;
    private RestErrorResolver errorResolver = new DefaultRestErrorResolver();
    private RestErrorConverter<?> errorConverter = new MapRestErrorConverter();

    public RestExceptionHandler() {
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    public void setErrorResolver(RestErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }

    public RestErrorResolver getErrorResolver() {
        return this.errorResolver;
    }

    public RestErrorConverter<?> getErrorConverter() {
        return this.errorConverter;
    }

    public void setErrorConverter(RestErrorConverter<?> errorConverter) {
        this.errorConverter = errorConverter;
    }

    public void afterPropertiesSet() throws Exception {
        this.ensureMessageConverters();
    }

    private void ensureMessageConverters() {
        ArrayList converters = new ArrayList();
        if(this.messageConverters != null && this.messageConverters.length > 0) {
            converters.addAll(CollectionUtils.arrayToList(this.messageConverters));
        }

        (new RestExceptionHandler.HttpMessageConverterHelper()).addDefaults(converters);
        this.allMessageConverters = converters;
    }

    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("==> 异常类型: " + ex.getClass().getName());
        ex.printStackTrace();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        RestErrorResolver resolver = this.getErrorResolver();
        RestError error = resolver.resolveError(webRequest, handler, ex);
        if(error == null) {
            return null;
        } else {
            ModelAndView mav = null;

            try {
                mav = this.getModelAndView(webRequest, handler, error);
            } catch (Exception var10) {
                log.info("Acquiring ModelAndView for Exception [" + ex + "] resulted in an exception.", var10);
            }

            return mav;
        }
    }

    protected ModelAndView getModelAndView(ServletWebRequest webRequest, Object handler, RestError error) throws Exception {
        this.applyStatusIfPossible(webRequest, error);
        Object body = error;
        RestErrorConverter converter = this.getErrorConverter();
        if(converter != null) {
            body = converter.convert(error);
        }

        return this.handleResponseBody(body, webRequest);
    }

    private void applyStatusIfPossible(ServletWebRequest webRequest, RestError error) {
        if(!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(error.getStatus().value());
        }

    }

    private ModelAndView handleResponseBody(Object body, ServletWebRequest webRequest) throws ServletException, IOException {
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
        List acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if(acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);
        ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
        Class bodyType = body.getClass();
        List converters = this.allMessageConverters;
        if(converters != null) {
            Iterator var8 = acceptedMediaTypes.iterator();

            while(var8.hasNext()) {
                MediaType acceptedMediaType = (MediaType)var8.next();
                Iterator var10 = converters.iterator();

                while(var10.hasNext()) {
                    HttpMessageConverter messageConverter = (HttpMessageConverter)var10.next();
                    if(messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        return new ModelAndView();
                    }
                }
            }
        }

        if(this.logger.isWarnEnabled()) {
            this.logger.warn("Could not find HttpMessageConverter that supports return type [" + bodyType + "] and " + acceptedMediaTypes);
        }

        return null;
    }

    private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        private HttpMessageConverterHelper() {
        }

        public void addDefaults(List<HttpMessageConverter<?>> converters) {
            this.addDefaultHttpMessageConverters(converters);
        }
    }
}
