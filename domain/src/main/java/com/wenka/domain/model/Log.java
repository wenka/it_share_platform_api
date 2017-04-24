package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 日志
 * Created by 文卡<wkwenka@gmail.com> on 2017/2/26.
 */
@Entity
@Table(name = "log")
public class Log extends AbstractVersionEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "context")
    private String context; // 内容

    @Column(name = "create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();// 创建日期

    @Transient
    private String userName;

    public String getUserName() {
        if (this.user != null) {
            return this.user.getName();
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
