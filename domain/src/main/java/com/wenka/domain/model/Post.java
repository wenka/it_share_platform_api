package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 发表物
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
@Entity
@Table(name = "post")
public class Post extends AbstractVersionEntity {


    public Set<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(Set<PostTag> postTags) {
        this.postTags = postTags;
    }

    public String getTagNames() {
        Set<String> tagNames = new HashSet<String>();
        if (this.tags != null && this.tags.size() > 0) {
            for (Tag tag : tags) {
                tagNames.add(tag.getName());
            }
        }
        return StringUtils.join(tagNames, ",");
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }

    public Set<Tag> getTags() {
        Set<Tag> tagNames = new HashSet<Tag>();
        if (postTags != null && postTags.size() > 0) {
            for (PostTag postTag : postTags) {
                tagNames.add(postTag.getTag());
            }
        }
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public BigInteger getIntegral() {
        return integral;
    }

    public void setIntegral(BigInteger integral) {
        this.integral = integral;
    }

    public boolean isAdoption() {
        return adoption;
    }

    public void setAdoption(boolean adoption) {
        this.adoption = adoption;
    }

    public enum PostType {
        博客, 头条, 提问, 评论;
    }

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType;

    @JSONField(serialize = false)
    @ManyToOne
    private Category category;//所属类别

    @JSONField(serialize = false)
    @ManyToOne
    private Post parent;//所属文章

    private String author;

    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    private String terms; //主题词

    private String brief;//摘要

    private String content;

    private BigInteger integral = BigInteger.ZERO;//积分

    private boolean adoption;//是否采纳/支持

    @OneToMany(mappedBy = "parent")
    @OrderBy("create_time desc ")
    private Set<Post> comments; //评论

    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags;

    @Transient
    private String tagNames;

    @Transient
    private Set<Tag> tags;

    @JSONField(serialize = false)
    @ManyToOne
    private User creator;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Attachment attachment;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", nullable = false)
    private Date updateTime = new Date();

    @Column(nullable = false)
    private Integer state = Integer.valueOf(0);

    @JSONField(serialize = false)
    @OneToMany(mappedBy = "owner")
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<PostAttachment> postAttachments; //附件

    @Column(name = "view_count")
    private BigInteger viewCount = BigInteger.ZERO; //访问量

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Post> getComments() {
        return comments;
    }

    public void setComments(Set<Post> comments) {
        this.comments = comments;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Set<PostAttachment> getPostAttachments() {
        return postAttachments;
    }

    public void setPostAttachments(Set<PostAttachment> postAttachments) {
        this.postAttachments = postAttachments;
    }

    @Transient
    private String categoryId;

    @Transient
    private String categoryName;
    @Transient
    private String parentId;

    @Transient
    private String parentTitle;
    @Transient
    private String creatorId;

    @Transient
    private String creatorName;
    @Transient
    private String attachmentIds;

    @Transient
    private String attachmentNames;

    public String getCategoryId() {
        return this.category == null ? this.categoryId : this.category.getId();
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return this.category == null ? this.categoryName : this.category.getName();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return this.parent == null ? this.parentId : this.parent.getId();
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentTitle() {
        return this.parent == null ? this.parentTitle : this.parent.getTitle();
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public String getCreatorId() {
        return this.creator == null ? this.creatorId : this.creator.getId();
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return this.creator == null ? this.creatorName : this.creator.getName();
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getAttachmentIds() {
        if (attachmentIds == null && postAttachments != null) {
            Set<String> ids = new HashSet<String>();
            for (PostAttachment postAttachment : postAttachments) {
                ids.add(postAttachment.getAttachment().getId());
            }
            attachmentIds = StringUtils.join(ids, ",");
        }
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getAttachmentNames() {
        if (attachmentNames == null && postAttachments != null) {
            Set<String> names = new HashSet<String>();
            for (PostAttachment postAttachments : postAttachments) {
                names.add(postAttachments.getAttachment().getRealName());
            }
            attachmentIds = StringUtils.join(names, ",");
        }
        return attachmentNames;
    }

    public void setAttachmentNames(String attachmentNames) {
        this.attachmentNames = attachmentNames;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    public BigInteger getViewCount() {
        return viewCount;
    }

    public void setViewCount(BigInteger viewCount) {
        this.viewCount = viewCount;
    }
}
