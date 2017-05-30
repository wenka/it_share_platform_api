package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 上传文件
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/17.
 */
@Entity
@Table(name = "attachment_bag")
public class AttachmentBag extends AbstractEntity {

    @ManyToOne
    private Attachment attachment;

    @Transient
    private String attachmentSrc;

    @ManyToOne
    private User creator;

    @ManyToOne
    private Category category;

    @Transient
    private String categoryName;

    public String getCategoryName() {
        return this.category != null ? this.category.getName() : this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum FileType {
        文档, 源码, 视频
    }

    @Enumerated(value = EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();

    private Integer state;

    private String remark;

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAttachmentSrc() {
        return this.attachment != null ? this.attachment.getRealName() : this.attachmentSrc;
    }

    public void setAttachmentSrc(String attachmentSrc) {
        this.attachmentSrc = attachmentSrc;
    }
}
