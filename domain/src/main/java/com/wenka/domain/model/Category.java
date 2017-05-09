package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Set;

/**
 * 类别
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
@Entity
@Table(name = "category")
public class Category extends AbstractVersionEntity {


    public enum CategoryType {
        类别, 栏目
    }

    @ManyToOne
    @JSONField(serialize = false)
    private Category parent; //父类

    @ManyToOne
    private Attachment portrait;

    private CategoryType categoryType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    @JSONField(serialize = false)
    private User creator;

    @Transient
    private String creatorName;

    @Transient
    private String creatorId;

    @Transient
    private String portraitId;

    @Transient
    private String portraitName;

    @Column(name = "name_", nullable = false)
    private String name;

    private String remark;

    @Transient
    private Boolean isParent;

    @Column(nullable = false)
    private Integer state = Integer.valueOf(0); // -1：删除  0：禁用  1：启用

    @OneToMany(mappedBy = "parent")
    @OrderBy("sort ASC ")
    private Set<Category> children;

    @Column(nullable = false)
    private Integer sort = Integer.valueOf(0);

    @Transient
    private Integer childrenCount = Integer.valueOf(0);

    @Transient
    private String parentId;

    @Transient
    private String parentName;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getChildrenCount() {
        return this.children == null ? this.childrenCount : this.children.size();
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public String getParentId() {
        return this.parent == null ? this.parentId : this.parent.getId();
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return this.parent == null ? this.parentName : this.parent.getName();
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Attachment getPortrait() {
        return portrait;
    }

    public void setPortrait(Attachment portrait) {
        this.portrait = portrait;
    }

    public String getPortraitId() {
        return this.portrait == null ? this.parentId : this.portrait.getId();
    }

    public void setPortraitId(String portraitId) {
        this.portraitId = portraitId;
    }

    public String getPortraitName() {
        return this.portrait == null ? this.portraitName : this.portrait.getRealName();
    }

    public void setPortraitName(String portraitName) {
        this.portraitName = portraitName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getParent() {
        return childrenCount.intValue() == 0 ? true : false;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

}
