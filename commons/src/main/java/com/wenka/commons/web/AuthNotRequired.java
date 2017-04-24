package com.wenka.commons.web;

import java.lang.annotation.*;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthNotRequired {
}

