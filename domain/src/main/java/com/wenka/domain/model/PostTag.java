package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 文章标签
 *
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Entity
@Table(name = "post_tag")
public class PostTag extends AbstractEntity {

    @JSONField(serialize = false)
    @ManyToOne
    private Tag tag;

    @JSONField(serialize = false)
    @ManyToOne
    private Post post;

    @Transient
    private String tagName;

    @Transient
    private String postTitle;

    @Transient
    private String tagId;

    @Transient
    private String postId;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getTagName() {
        return this.tag == null ? this.tagName : this.tag.getName();
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getPostTitle() {
        return this.post == null ? this.postTitle : this.post.getTitle();
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getTagId() {
        return this.tag == null ? this.tagId : this.tag.getId();
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getPostId() {
        return this.post == null ? this.postId : this.post.getId();
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
