package com.wenka.commons.web.exception;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
public class Unauthorized extends RuntimeException {
    public Unauthorized() {
        super("未认证");
    }

    public Unauthorized(String message) {
        super(message);
    }
}
