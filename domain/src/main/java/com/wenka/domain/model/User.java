package com.wenka.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * 账户
 * Created by 文卡<wkwenka@gmail.com> on 2017/2/26.
 */
@Entity
@Table(name = "users")
public class User extends AbstractVersionEntity {

    @Column(name = "account", nullable = false)
    @Length(min = 5, max = 36)
    private String account; //账户

    @Column(name = "password", nullable = false)
    @Length(min = 6, max = 50)
    private String password;//密码

    @Column(name = "name_", nullable = false)
    @Length(min = 2, max = 10)
    private String name;//姓名

    @Column(name = "spell", nullable = false)
    private String spell;//拼写

    @Column(name = "kind_code")
    private Integer kindCode;//类型

    @Column(name = "tel")
    private String tel;//电话

    @Column(name = "email")
    private String email;//email

    @Column(name = "address")
    private String address;//地址

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();//创建时间

    @Column(name = "state", nullable = false)
    private Integer state = Integer.valueOf(0); // -1：删除  0：未启用 1：启用

    @Column(name = "remark")
    private String remark; //备注

    public enum Gender{
        male,female,none
    }
    private Gender gender; //性别

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment; //头像

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Integer getKindCode() {
        return kindCode;
    }

    public void setKindCode(Integer kindCode) {
        this.kindCode = kindCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
