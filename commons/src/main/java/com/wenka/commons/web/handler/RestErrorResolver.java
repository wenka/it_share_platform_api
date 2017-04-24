package com.wenka.commons.web.handler;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
public interface RestErrorResolver {
    RestError resolveError(ServletWebRequest var1, Object var2, Exception var3);
}
