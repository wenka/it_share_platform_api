package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户动态
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/15.
 */
@Entity
@Table(name = "user_dynamic")
public class UserDynamic extends AbstractEntity {

    @ManyToOne
//    @JoinColumn(name = "owner_id")
    private User owner; //所属用户

    @ManyToOne
//    @JoinColumn(name = "post_id")
    private Post post; // 动态所在地

    private String msg; //消息

    @Transient
    private String postId;

    @Transient
    private String ownerId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();

    private Integer state = Integer.valueOf(0);

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPostId() {
        return this.post != null ? this.post.getId() : this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getOwnerId() {
        return this.owner != null ? this.owner.getId() : this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
