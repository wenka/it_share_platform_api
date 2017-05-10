package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户粉丝类
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/10.
 */
@Entity
@Table(name = "user_fans")
public class UserFans extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; //所属者

    @ManyToOne
    @JoinColumn(name = "fans_id")
    private User fans; //粉丝

    @Column(name = "create_time",nullable = false)
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime = new Date();

    @Transient
    private String ownerId;

    @Transient
    private String ownerName;

    @Transient
    private String fansId;

    @Transient
    private String fansName;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getFans() {
        return fans;
    }

    public void setFans(User fans) {
        this.fans = fans;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOwnerId() {
        return this.owner != null ?this.owner.getId():"";
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return this.owner != null ?this.owner.getName():"";
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFansId() {
        return this.fans != null ?this.fans.getId():"";
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public String getFansName() {
        return this.fans != null ?this.fans.getName():"";
    }

    public void setFansName(String fansName) {
        this.fansName = fansName;
    }
}
