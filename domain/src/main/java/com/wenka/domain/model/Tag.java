package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 标签
 *
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Entity
@Table(name = "tag")
public class Tag extends AbstractVersionEntity {

    private String name;

    private String spell;

    @ManyToOne
    private User user;

    @Transient
    private String userId;

    @Transient
    private String userName;

    @Column(name = "create_time", nullable = false)
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime = new Date();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return this.user == null ? this.userId : this.user.getId();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.user == null ? this.userName : this.user.getName();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
