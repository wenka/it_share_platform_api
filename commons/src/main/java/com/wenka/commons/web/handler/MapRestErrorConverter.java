package com.wenka.commons.web.handler;

import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class MapRestErrorConverter implements RestErrorConverter<Map> {
    private static final String DEFAULT_STATUS_KEY = "status";
    private static final String DEFAULT_CODE_KEY = "code";
    private static final String DEFAULT_MESSAGE_KEY = "message";
    private static final String DEFAULT_DEVELOPER_MESSAGE_KEY = "developerMessage";
    private static final String DEFAULT_MORE_INFO_URL_KEY = "moreInfoUrl";
    private String statusKey = "status";
    private String codeKey = "code";
    private String messageKey = "message";
    private String developerMessageKey = "developerMessage";
    private String moreInfoUrlKey = "moreInfoUrl";

    public MapRestErrorConverter() {
    }

    public Map convert(RestError re) {
        Map m = this.createMap();
        HttpStatus status = re.getStatus();
        m.put(this.getStatusKey(), Integer.valueOf(status.value()));
        int code = re.getCode();
        if(code > 0) {
            m.put(this.getCodeKey(), Integer.valueOf(code));
        }

        String message = re.getMessage();
        if(message != null) {
            m.put(this.getMessageKey(), message);
        }

        String devMsg = re.getDeveloperMessage();
        if(devMsg != null) {
            m.put(this.getDeveloperMessageKey(), devMsg);
        }

        String moreInfoUrl = re.getMoreInfoUrl();
        if(moreInfoUrl != null) {
            m.put(this.getMoreInfoUrlKey(), moreInfoUrl);
        }

        return m;
    }

    protected Map<String, Object> createMap() {
        return new LinkedHashMap();
    }

    public String getStatusKey() {
        return this.statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    public String getCodeKey() {
        return this.codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getDeveloperMessageKey() {
        return this.developerMessageKey;
    }

    public void setDeveloperMessageKey(String developerMessageKey) {
        this.developerMessageKey = developerMessageKey;
    }

    public String getMoreInfoUrlKey() {
        return this.moreInfoUrlKey;
    }

    public void setMoreInfoUrlKey(String moreInfoUrlKey) {
        this.moreInfoUrlKey = moreInfoUrlKey;
    }
}
