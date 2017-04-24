package com.wenka.commons.web.handler;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class RestError {
    private final HttpStatus status;
    private final int code;
    private final String message;
    private final String developerMessage;
    private final String moreInfoUrl;
    private final Throwable throwable;

    public RestError(HttpStatus status, int code, String message, String developerMessage, String moreInfoUrl, Throwable throwable) {
        if(status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        } else {
            this.status = status;
            this.code = code;
            this.message = message;
            this.developerMessage = developerMessage;
            this.moreInfoUrl = moreInfoUrl;
            this.throwable = throwable;
        }
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDeveloperMessage() {
        return this.developerMessage;
    }

    public String getMoreInfoUrl() {
        return this.moreInfoUrl;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public ModelAndView asModelAndView() {
        HashMap map = new HashMap();
        map.put("status", this.status);
        map.put("code", Integer.valueOf(this.code));
        map.put("message", this.message);
        map.put("developerMessage", this.developerMessage);
        map.put("moreInfoUrl", this.moreInfoUrl);
        map.put("throwable", this.throwable);
        FastJsonJsonView jsonView = new FastJsonJsonView();
        return new ModelAndView(jsonView, map);
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof RestError)) {
            return false;
        } else {
            RestError re = (RestError)o;
            return ObjectUtils.nullSafeEquals(this.getStatus(), re.getStatus()) && this.getCode() == re.getCode() && ObjectUtils.nullSafeEquals(this.getMessage(), re.getMessage()) && ObjectUtils.nullSafeEquals(this.getDeveloperMessage(), re.getDeveloperMessage()) && ObjectUtils.nullSafeEquals(this.getMoreInfoUrl(), re.getMoreInfoUrl()) && ObjectUtils.nullSafeEquals(this.getThrowable(), re.getThrowable());
        }
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(new Object[]{this.getStatus(), Integer.valueOf(this.getCode()), this.getMessage(), this.getDeveloperMessage(), this.getMoreInfoUrl(), this.getThrowable()});
    }

    public String toString() {
        return this.getStatus().value() + " (" + this.getStatus().getReasonPhrase() + " )";
    }

    public static class Builder {
        private HttpStatus status;
        private int code;
        private String message;
        private String developerMessage;
        private String moreInfoUrl;
        private Throwable throwable;

        public Builder() {
        }

        public RestError.Builder setStatus(int statusCode) {
            this.status = HttpStatus.valueOf(statusCode);
            return this;
        }

        public RestError.Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public RestError.Builder setCode(int code) {
            this.code = code;
            return this;
        }

        public RestError.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public RestError.Builder setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public RestError.Builder setMoreInfoUrl(String moreInfoUrl) {
            this.moreInfoUrl = moreInfoUrl;
            return this;
        }

        public RestError.Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public RestError build() {
            if(this.status == null) {
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return new RestError(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl, this.throwable);
        }
    }
}
