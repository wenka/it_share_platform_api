package com.wenka.commons.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.util.*;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class DefaultRestErrorResolver implements RestErrorResolver, MessageSourceAware, InitializingBean {
    public static final String DEFAULT_EXCEPTION_MESSAGE_VALUE = "_exmsg";
    public static final String DEFAULT_MESSAGE_VALUE = "_msg";
    private static final Logger log = LoggerFactory.getLogger(DefaultRestErrorResolver.class);
    private Map<String, RestError> exceptionMappings = Collections.emptyMap();
    private Map<String, String> exceptionMappingDefinitions = Collections.emptyMap();
    private MessageSource messageSource;
    private LocaleResolver localeResolver;
    private String defaultMoreInfoUrl;
    private boolean defaultEmptyCodeToStatus = true;
    private String defaultDeveloperMessage = "_exmsg";

    public DefaultRestErrorResolver() {
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleResolver(LocaleResolver resolver) {
        this.localeResolver = resolver;
    }

    public void setExceptionMappingDefinitions(Map<String, String> exceptionMappingDefinitions) {
        this.exceptionMappingDefinitions = exceptionMappingDefinitions;
    }

    public void setDefaultMoreInfoUrl(String defaultMoreInfoUrl) {
        this.defaultMoreInfoUrl = defaultMoreInfoUrl;
    }

    public void setDefaultEmptyCodeToStatus(boolean defaultEmptyCodeToStatus) {
        this.defaultEmptyCodeToStatus = defaultEmptyCodeToStatus;
    }

    public void setDefaultDeveloperMessage(String defaultDeveloperMessage) {
        this.defaultDeveloperMessage = defaultDeveloperMessage;
    }

    public void afterPropertiesSet() throws Exception {
        Map definitions = this.createDefaultExceptionMappingDefinitions();
        if(this.exceptionMappingDefinitions != null && !this.exceptionMappingDefinitions.isEmpty()) {
            definitions.putAll(this.exceptionMappingDefinitions);
        }

        this.exceptionMappings = this.toRestErrors(definitions);
    }

    protected final Map<String, String> createDefaultExceptionMappingDefinitions() {
        LinkedHashMap m = new LinkedHashMap();
        this.applyDef(m, (Class)HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        this.applyDef(m, (Class)MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        this.applyDef(m, (Class)TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        this.applyDef(m, (String)"javax.validation.ValidationException", HttpStatus.BAD_REQUEST);
        this.applyDef(m, (Class)NoSuchRequestHandlingMethodException.class, HttpStatus.NOT_FOUND);
        this.applyDef(m, (String)"org.hibernate.ObjectNotFoundException", HttpStatus.NOT_FOUND);
        this.applyDef(m, (Class)HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
        this.applyDef(m, (Class)HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        this.applyDef(m, (String)"org.springframework.dao.DataIntegrityViolationException", HttpStatus.CONFLICT);
        this.applyDef(m, (Class)HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return m;
    }

    private void applyDef(Map<String, String> m, Class clazz, HttpStatus status) {
        this.applyDef(m, clazz.getName(), status);
    }

    private void applyDef(Map<String, String> m, String key, HttpStatus status) {
        m.put(key, this.definitionFor(status));
    }

    private String definitionFor(HttpStatus status) {
        return status.value() + ", " + "_exmsg";
    }

    public RestError resolveError(ServletWebRequest request, Object handler, Exception ex) {
        RestError template = this.getRestErrorTemplate(ex);
        if(template == null) {
            return null;
        } else {
            RestError.Builder builder = new RestError.Builder();
            builder.setStatus(this.getStatusValue(template, request, ex));
            builder.setCode(this.getCode(template, request, ex));
            builder.setMoreInfoUrl(this.getMoreInfoUrl(template, request, ex));
            builder.setThrowable(ex);
            String msg = this.getMessage(template, request, ex);
            if(msg != null) {
                builder.setMessage(msg);
            }

            msg = this.getDeveloperMessage(template, request, ex);
            if(msg != null) {
                builder.setDeveloperMessage(msg);
            }

            return builder.build();
        }
    }

    protected int getStatusValue(RestError template, ServletWebRequest request, Exception ex) {
        return template.getStatus().value();
    }

    protected int getCode(RestError template, ServletWebRequest request, Exception ex) {
        int code = template.getCode();
        if(code <= 0 && this.defaultEmptyCodeToStatus) {
            code = this.getStatusValue(template, request, ex);
        }

        return code;
    }

    protected String getMoreInfoUrl(RestError template, ServletWebRequest request, Exception ex) {
        String moreInfoUrl = template.getMoreInfoUrl();
        if(moreInfoUrl == null) {
            moreInfoUrl = this.defaultMoreInfoUrl;
        }

        return moreInfoUrl;
    }

    protected String getMessage(RestError template, ServletWebRequest request, Exception ex) {
        return this.getMessage(template.getMessage(), request, ex);
    }

    protected String getDeveloperMessage(RestError template, ServletWebRequest request, Exception ex) {
        String devMsg = template.getDeveloperMessage();
        if(devMsg == null && this.defaultDeveloperMessage != null) {
            devMsg = this.defaultDeveloperMessage;
        }

        if("_msg".equals(devMsg)) {
            devMsg = template.getMessage();
        }

        return this.getMessage(devMsg, request, ex);
    }

    protected String getMessage(String msg, ServletWebRequest webRequest, Exception ex) {
        if(msg != null) {
            if(msg.equalsIgnoreCase("null") || msg.equalsIgnoreCase("off")) {
                return null;
            }

            if(msg.equalsIgnoreCase("_exmsg")) {
                msg = ex.getMessage();
            }

            if(this.messageSource != null) {
                Locale locale = null;
                if(this.localeResolver != null) {
                    locale = this.localeResolver.resolveLocale(webRequest.getRequest());
                }

                msg = this.messageSource.getMessage(msg, (Object[])null, msg, locale);
            }
        }

        return msg;
    }

    private RestError getRestErrorTemplate(Exception ex) {
        Map mappings = this.exceptionMappings;
        if(CollectionUtils.isEmpty(mappings)) {
            return null;
        } else {
            RestError template = null;
            String dominantMapping = null;
            int deepest = 2147483647;
            Iterator var6 = mappings.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry entry = (Map.Entry)var6.next();
                String key = (String)entry.getKey();
                int depth = this.getDepth(key, ex);
                if(depth >= 0 && depth < deepest) {
                    deepest = depth;
                    dominantMapping = key;
                    template = (RestError)entry.getValue();
                }
            }

            if(template != null && log.isDebugEnabled()) {
                log.debug("Resolving to RestError template \'" + template + "\' for exception of type [" + ex.getClass().getName() + "], based on exception mapping [" + dominantMapping + "]");
            }

            return template;
        }
    }

    protected int getDepth(String exceptionMapping, Exception ex) {
        return this.getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class exceptionClass, int depth) {
        return exceptionClass.getName().contains(exceptionMapping)?depth:(exceptionClass.equals(Throwable.class)?-1:this.getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1));
    }

    protected Map<String, RestError> toRestErrors(Map<String, String> smap) {
        if(CollectionUtils.isEmpty(smap)) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap map = new LinkedHashMap(smap.size());
            Iterator var3 = smap.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry entry = (Map.Entry)var3.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                RestError template = this.toRestError(value);
                map.put(key, template);
            }

            return map;
        }
    }

    protected RestError toRestError(String exceptionConfig) {
        String[] values = StringUtils.commaDelimitedListToStringArray(exceptionConfig);
        if(values != null && values.length != 0) {
            RestError.Builder builder = new RestError.Builder();
            boolean statusSet = false;
            boolean codeSet = false;
            boolean msgSet = false;
            boolean devMsgSet = false;
            boolean moreInfoSet = false;
            String[] var9 = values;
            int var10 = values.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                String value = var9[var11];
                String trimmedVal = StringUtils.trimWhitespace(value);
                String[] pair = StringUtils.split(trimmedVal, "=");
                if(pair != null) {
                    String var18 = StringUtils.trimWhitespace(pair[0]);
                    if(!StringUtils.hasText(var18)) {
                        var18 = null;
                    }

                    String pairValue = StringUtils.trimWhitespace(pair[1]);
                    if(!StringUtils.hasText(pairValue)) {
                        pairValue = null;
                    }

                    int code;
                    if("status".equalsIgnoreCase(var18)) {
                        code = getRequiredInt(var18, pairValue);
                        builder.setStatus(code);
                        statusSet = true;
                    } else if("code".equalsIgnoreCase(var18)) {
                        code = getRequiredInt(var18, pairValue);
                        builder.setCode(code);
                        codeSet = true;
                    } else if("msg".equalsIgnoreCase(var18)) {
                        builder.setMessage(pairValue);
                        msgSet = true;
                    } else if("devMsg".equalsIgnoreCase(var18)) {
                        builder.setDeveloperMessage(pairValue);
                        devMsgSet = true;
                    } else if("infoUrl".equalsIgnoreCase(var18)) {
                        builder.setMoreInfoUrl(pairValue);
                        moreInfoSet = true;
                    }
                } else {
                    int val;
                    if(!statusSet) {
                        val = getInt("status", trimmedVal);
                        if(val > 0) {
                            builder.setStatus(val);
                            statusSet = true;
                            continue;
                        }
                    }

                    if(!codeSet) {
                        val = getInt("code", trimmedVal);
                        if(val > 0) {
                            builder.setCode(val);
                            codeSet = true;
                            continue;
                        }
                    }

                    if(!moreInfoSet && trimmedVal.toLowerCase().startsWith("http")) {
                        builder.setMoreInfoUrl(trimmedVal);
                        moreInfoSet = true;
                    } else if(!msgSet) {
                        builder.setMessage(trimmedVal);
                        msgSet = true;
                    } else if(!devMsgSet) {
                        builder.setDeveloperMessage(trimmedVal);
                        devMsgSet = true;
                    } else if(!moreInfoSet) {
                        builder.setMoreInfoUrl(trimmedVal);
                        moreInfoSet = true;
                    }
                }
            }

            return builder.build();
        } else {
            throw new IllegalStateException("Invalid config mapping.  Exception names must map to a string configuration.");
        }
    }

    private static int getRequiredInt(String key, String value) {
        try {
            int e = Integer.valueOf(value).intValue();
            return Math.max(-1, e);
        } catch (NumberFormatException var4) {
            String msg = "Configuration element \'" + key + "\' requires an integer value.  The value specified: " + value;
            throw new IllegalArgumentException(msg, var4);
        }
    }

    private static int getInt(String key, String value) {
        try {
            return getRequiredInt(key, value);
        } catch (IllegalArgumentException var3) {
            return 0;
        }
    }
}
