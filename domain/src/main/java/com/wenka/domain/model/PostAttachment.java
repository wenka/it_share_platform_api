package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 发表物附件
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
@Entity
@Table(name = "post_attachment")
public class PostAttachment extends AbstractEntity {

    @ManyToOne
    @JSONField(serialize = false)
    private Post owner;

    @ManyToOne
    private Attachment attachment;

    @Transient
    private String ownerId;

    @Transient
    private String ownerTitle;

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Post getOwner() {
        return owner;
    }

    public void setOwner(Post owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return this.owner == null ? this.ownerId : this.owner.getId();
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerTitle() {
        return this.owner == null ? this.ownerTitle : this.owner.getTitle();
    }

    public void setOwnerTitle(String ownerTitle) {
        this.ownerTitle = ownerTitle;
    }
}
