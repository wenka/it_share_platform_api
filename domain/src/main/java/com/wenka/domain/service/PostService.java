package com.wenka.domain.service;

import com.wenka.domain.dao.PostDao;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    /**
     * 新增修改文章
     *
     * @param post
     */
    public void saveOrUpdate(Post post) {

        if (post.getAttachmentIds() != null) {

            post = postDao.getAttachments(post);

        }

        postDao.saveOrUpdate(post);

    }

    /**
     * 读取单一文章
     *
     * @param id
     * @return
     */
    public Post get(String id) {

        return postDao.get(id);

    }

    /**
     * 标记删除文章
     *
     * @param id
     */
    public void delete(String id) {
        Post post = postDao.get(id);
        post.setState(-1);// 更改对象状态为禁用
        postDao.update(post);
    }

    private HqlArgs genHqlArgs(String param, List<String> categoryIds, List<Integer> states) {
        param = StringUtils.trimToEmpty(param);

        Map<String, Object> args = new HashMap<String, Object>();
        String hql = "from Post p where 1=1";

        if (StringUtils.isNotBlank(param)) {
            hql += " and (p.name like :arg or p.remark like :arg or p.author like :arg or p.title like :arg or p.subTitle like :arg)";
            args.put("arg", "%" + param + "%");
        }

        if (categoryIds != null && categoryIds.size() > 0) {
            if (categoryIds.size() == 1) {
                hql += " and p.category.id = :categoryIds";
                args.put("categoryIds", categoryIds.get(0));
            } else {
                hql += " and p.category.id in :categoryIds";
                args.put("categoryIds", categoryIds);
            }
        }

        if (states != null && states.size() > 0) {
            if (states.size() == 0) {
                hql += " and p.state = :state";
                args.put("state", states.get(0));
            } else {
                hql += " and p.state in :states";
                args.put("states", states);
            }
        }

        hql += " and p.postType=:postType";
        args.put("postType", Post.PostType.文章);

        return new HqlArgs(hql, args);

    }

    /**
     * 获取文章集合
     *
     * @param param
     * @param categoryIds
     * @param states
     * @param startIndex
     * @param rows
     * @return
     */
    public List<Post> getList(String param, List<String> categoryIds, List<Integer> states, Integer startIndex, Integer rows) {
        HqlArgs hqlArgs = genHqlArgs(param, categoryIds, states);
        List<Post> list = postDao.findByNamedParam(hqlArgs.getHql(), startIndex, rows, hqlArgs.getArgs());
        return list;
    }

    public int getListSize(String param, List<String> categoryIds, List<Integer> states) {
        HqlArgs hqlArgs = genHqlArgs(param, categoryIds, states);
        List<Post> list = postDao.findByNamedParam(hqlArgs.getHql(), hqlArgs.getArgs());
        return list.size();
    }
}
