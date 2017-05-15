package com.wenka.domain.service;

import com.wenka.domain.dao.PostDao;
import com.wenka.domain.dao.PostTagDao;
import com.wenka.domain.dao.TagDao;
import com.wenka.domain.model.Post;
import com.wenka.domain.model.PostTag;
import com.wenka.domain.model.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 文章标签
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/15.
 */
@Service
public class PostTagService {

    @Autowired
    private PostTagDao postTagDao;

    @Autowired
    private TagService tagService;

    public void save(Post post){
        Set<Tag> tags = post.getTags();
        if (tags != null && tags.size() > 0){
           for (Tag tag : tags){
               tag.setUser(post.getCreator());
               Tag save = tagService.save(tag);
               PostTag postTag = new PostTag();
               postTag.setTag(save);
               postTag.setPost(post);
               postTagDao.save(postTag);
           }
        }
    }
}
