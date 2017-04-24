package com.wenka.commons.web;

import com.alibaba.fastjson.JSONWriter;
import com.wenka.commons.web.exception.Unauthorized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class MHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ERRORS = "errors";
    private static final String ERRORS_JSON = "errors_json";
    private static final String _THROWABLE = "exception";

    public MHandlerExceptionResolver() {
    }

    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LinkedHashMap resultMap = new LinkedHashMap();
        ex.printStackTrace();
        short status = 500;
        HashMap errorMap = null;

        try {
            if (ex instanceof NoSuchRequestHandlingMethodException) {
                status = 404;
            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                status = 405;
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                status = 415;
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                status = 406;
            } else if (ex instanceof MissingPathVariableException) {
                status = 500;
            } else if (ex instanceof MissingServletRequestParameterException) {
                status = 400;
            } else if (ex instanceof ServletRequestBindingException) {
                status = 400;
            } else if (ex instanceof ConversionNotSupportedException) {
                status = 500;
            } else if (ex instanceof TypeMismatchException) {
                status = 400;
            } else if (ex instanceof HttpMessageNotReadableException) {
                status = 400;
            } else if (ex instanceof HttpMessageNotWritableException) {
                status = 500;
            } else if (ex instanceof MethodArgumentNotValidException) {
                status = 400;
            } else if (ex instanceof MissingServletRequestPartException) {
                status = 400;
            } else if (ex instanceof BindException) {
                status = 400;
                BindException e = (BindException) ex;
                errorMap = new HashMap();
                Iterator var9 = e.getBindingResult().getFieldErrors().iterator();

                while (var9.hasNext()) {
                    FieldError fieldError = (FieldError) var9.next();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            } else if (ex instanceof NoHandlerFoundException) {
                status = 404;
            } else if (ex instanceof Unauthorized) {
                status = 401;
            }
        } catch (Exception var12) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", var12);
            }

            status = 500;
        }

        resultMap.put("errors", ex.getMessage());
        resultMap.put("errors_json", errorMap);
        resultMap.put("exception", ex.getClass().getName());
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            JSONWriter e1 = new JSONWriter(response.getWriter());
            e1.writeObject(resultMap);
            response.flushBuffer();
        } catch (Exception var11) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", var11);
            }
        }

        return null;
    }
}
