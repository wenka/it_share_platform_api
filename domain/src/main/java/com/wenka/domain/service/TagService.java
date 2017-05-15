package com.wenka.domain.service;

import com.wenka.domain.dao.TagDao;
import com.wenka.domain.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/15.
 */
@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    /**
     * 保存
     *
     * @param tag
     */
    public Tag save(Tag tag) {
        Tag existed = this.getExisted(tag);
        if (existed == null) {
            this.tagDao.save(tag);
            return tag;
        }else {
            return existed;
        }
    }

    /***
     * 通过id 查询tag
     * @param id
     */
    public Tag get(String id) {
        return tagDao.get(id);
    }

    public Tag getExisted(Tag tag) {
        String name = tag.getName();
        String userId = tag.getUserId();

        String hql = "from Tag where name = ? and user.id = ?";

        List<Tag> tags = this.tagDao.find(hql, name, userId);

        if (tags != null && tags.size() > 0) {
            return tags.get(0);//存在
        } else {
            return null;//不存在
        }

    }
}
