package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 附件
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
@Entity
@Table(name = "attachment")
public class Attachment extends AbstractEntity {

    @Column(name = "origina_name", nullable = false)
    private String originalName; //原生名字

    @Column(name = "content_type", nullable = false)
    private String contentType;//类型

    @Column(name = "real_name", nullable = false)
    private String realName;//真实名字

    @Column(name = "create_time",nullable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
