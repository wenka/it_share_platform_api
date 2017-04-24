package com.wenka.commons.web;

import com.wenka.commons.web.exception.Unauthorized;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class MHandlerInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String VOIDJSON = "{\"result\": \"OK\"}";
    private String secretKey;
    private String signKey;

    public MHandlerInterceptor() {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        } else {
            HandlerMethod hm = (HandlerMethod)handler;
            if(hm.getBeanType().isAnnotationPresent(AuthNotRequired.class)) {
                return true;
            } else if(hm.getMethod().isAnnotationPresent(AuthNotRequired.class)) {
                return true;
            } else {
                String token = null;
                Cookie[] cookies = request.getCookies();
                if(cookies != null) {
                    Cookie[] authToken = cookies;
                    int var8 = cookies.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        Cookie cookie = authToken[var9];
                        if("m-at-id".equals(cookie.getName())) {
                            token = cookie.getValue();
                        }
                    }
                }

                if(token == null) {
                    token = request.getHeader("m-at-id");
                    token = token == null?request.getHeader("X-m-at-id"):null;
                }

                if(token == null) {
                    token = request.getParameter("m-at-id");
                }

                if(StringUtils.isBlank(token)) {
                    this.cleanCookie((AuthToken)null, request, response);
                    throw new Unauthorized();
                } else {
                    AuthToken var11 = new AuthToken(token, this.secretKey, this.signKey);
                    if(var11.getExpiryTime() == null) {
                        this.cleanCookie(var11, request, response);
                        throw new Unauthorized();
                    } else if(var11.getExpiryTime().getTime() <= (new Date()).getTime()) {
                        this.cleanCookie(var11, request, response);
                        throw new Unauthorized();
                    } else if(StringUtils.isBlank(var11.getUid())) {
                        this.cleanCookie(var11, request, response);
                        throw new Unauthorized();
                    } else {
                        request.setAttribute("authToken", var11);
                        this.logger.info("==> auth token: " + var11.toString());
                        return true;
                    }
                }
            }
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(!response.isCommitted()) {
            if(modelAndView != null) {
                return;
            }

            if(DefaultServletHttpRequestHandler.class == handler.getClass()) {
                return;
            }

            response.setStatus(200);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write("{\"result\": \"OK\"}");
            response.flushBuffer();
        }

    }

    private void cleanCookie(AuthToken authToken, HttpServletRequest request, HttpServletResponse response) {
        if(authToken != null) {
            Cookie var9 = authToken.toCookie();
            var9.setMaxAge(0);
            response.addCookie(var9);
        } else {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                Cookie[] var5 = cookies;
                int var6 = cookies.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    Cookie cookie = var5[var7];
                    if("m-at-id".equals(cookie.getName())) {
                        cookie.setValue((String)null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
            }

        }
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }
}
