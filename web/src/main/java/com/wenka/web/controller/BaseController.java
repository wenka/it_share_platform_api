package com.wenka.web.controller;

import com.alibaba.fastjson.JSON;
import com.wenka.commons.web.AuthToken;
import com.wenka.domain.model.User;
import com.wenka.domain.service.LogService;
import com.wenka.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final int ROWS_PER_PAGE = 20;
    @Autowired
    protected LogService logService;
    @Autowired
    private UserService userService;

    protected User currentUser;
    protected String currentUserId;

    public BaseController() {
    }

    @ModelAttribute
    private void getCurrentUser(HttpServletRequest request) {
        Object o = request.getAttribute("authToken");
        if(o != null) {
            AuthToken authToken = (AuthToken)o;
            this.currentUserId = authToken.getUid();
            if(this.currentUserId != null) {
                this.currentUser = this.userService.get(this.currentUserId);
            }
        }
    }

    public void saveLog(String info) {
        this.logService.save(info, this.currentUser);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleValidationException(MethodArgumentNotValidException exception) {
        this.logger.info(exception.getMessage(), exception.getCause());
        HashMap errors = new HashMap();
        Iterator map = exception.getBindingResult().getFieldErrors().iterator();

        while(map.hasNext()) {
            FieldError fieldError = (FieldError)map.next();
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        HashMap map1 = new HashMap();
        map1.put("status", Integer.valueOf(400));
        map1.put("code", Integer.valueOf(400));
        map1.put("message", JSON.toJSONString(errors));
        map1.put("developerMessage", JSON.toJSONString(errors));
        map1.put("developerJson", errors);
        map1.put("moreInfoUrl", "mailto:wenka@gmail.com");
        map1.put("throwable", exception.getCause());
        return map1;
    }
}

