package com.wenka.commons.web;

import com.wenka.commons.util.Convertor;
import com.wenka.commons.util.SecureCoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class AuthToken implements Serializable {
    private final Logger logger;
    public static final String NAME = "m-at-id";
    private String id;
    private String scope;
    private String uid;
    private String device;
    private Date expiryTime;
    private int timeout;
    private String secretKey;
    private String signKey;

    public AuthToken() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.scope = "/";
        this.device = "n";
        this.timeout = 5;
        this.secretKey = "1o2vdS5EPWGAop6GBGpL2g==";
        this.signKey = "iMap5KIBJKhr8Ix7D+K93LPKHJxXt6v2SGesqoX8vUK53v47RGECOABh2BS6om/b/Sp+fE5YgbDJR58Q7v3lnA==";
        this.setTimeout(this.timeout);
    }

    public AuthToken(String encrypted) {
        this(encrypted, (String)null, (String)null);
    }

    public AuthToken(String encrypted, String secretKey, String signKey) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.scope = "/";
        this.device = "n";
        this.timeout = 5;
        this.secretKey = "1o2vdS5EPWGAop6GBGpL2g==";
        this.signKey = "iMap5KIBJKhr8Ix7D+K93LPKHJxXt6v2SGesqoX8vUK53v47RGECOABh2BS6om/b/Sp+fE5YgbDJR58Q7v3lnA==";
        if(secretKey != null) {
            this.secretKey = secretKey;
        }

        if(signKey != null) {
            this.signKey = signKey;
        }

        this.fromString(encrypted);
    }

    public void fromString(String encrypted) {
        String deciphered = null;

        try {
            deciphered = SecureCoder.aesDecrypt(encrypted, Base64.decodeBase64(this.secretKey));
        } catch (Exception var15) {
            this.logger.info(var15.getMessage(), var15.getCause());
        }

        if(deciphered != null && deciphered.length() >= 40) {
            String sign = deciphered.substring(0, 40);
            String token = deciphered.substring(40);
            if(!StringUtils.isBlank(token)) {
                String sign_ = null;

                try {
                    sign_ = SecureCoder.hmacSHA1(token, this.signKey);
                } catch (Exception var14) {
                    this.logger.info(var14.getMessage(), var14.getCause());
                }

                if(StringUtils.equals(sign, sign_)) {
                    String[] keyValues = StringUtils.splitByWholeSeparator(token, "|");
                    String[] var7 = keyValues;
                    int var8 = keyValues.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        String keyValue = var7[var9];
                        String[] kv = StringUtils.splitByWholeSeparator(keyValue, "_");
                        if(kv != null && kv.length == 2) {
                            String k = kv[0];
                            String v = kv[1];
                            if("id".equals(k)) {
                                this.setId(v);
                            } else if("scope".equals(k)) {
                                this.setScope(v);
                            } else if("uid".equals(k)) {
                                this.setUid(v);
                            } else if("device".equals(k)) {
                                this.setDevice(v);
                            } else if("expiryTime".equals(k)) {
                                this.expiryTime = Convertor.parseTime(v);
                            } else if("timeout".equals(k)) {
                                this.timeout = Integer.parseInt(v);
                            }
                        }
                    }

                }
            }
        }
    }

    public Cookie toCookie() {
        Cookie result = new Cookie("m-at-id", this.toString());
        result.setPath(this.scope);
        result.setMaxAge(this.timeout * 60);
        result.setHttpOnly(true);
        return result;
    }

    public String toString() {
        String s = String.format("|%s_%s|%s_%s|%s_%s|%s_%s|%s_%s|%s_%s|", new Object[]{"id", this.getId(), "scope", this.getScope(), "uid", this.getUid(), "device", this.getDevice(), "expiryTime", Convertor.formatTime(this.getExpiryTime()), "timeout", Integer.valueOf(this.getTimeout())});
        String sign = null;

        try {
            sign = SecureCoder.hmacSHA1(s, this.signKey);
        } catch (Exception var6) {
            this.logger.info(var6.getMessage(), var6.getCause());
        }

        if(sign == null) {
            return null;
        } else {
            String encrypted = null;

            try {
                encrypted = SecureCoder.aesEncrypt(sign + s, Base64.decodeBase64(this.secretKey));
            } catch (Exception var5) {
                this.logger.info(var5.getMessage(), var5.getCause());
            }

            return encrypted;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getExpiryTime() {
        return this.expiryTime;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        Date d = new Date();
        d.setTime(d.getTime() + (long)(timeout * 60 * 1000));
        this.expiryTime = d;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSignKey() {
        return this.signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public static void main(String[] args) throws Exception {
        AuthToken token = new AuthToken();
        token.setId(UUID.randomUUID().toString());
        token.setScope(RandomStringUtils.random(8));
        token.setUid("121");
        token.setDevice("111");
        token.setTimeout(1440);
        String s = token.toString();
        System.err.println(s);
        AuthToken token1 = new AuthToken(s);
        System.err.println(token1.getUid());
    }
}