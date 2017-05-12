package com.wenka.domain.service;

import com.wenka.domain.dao.PostDao;
import com.wenka.domain.model.Category;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.Post;
import javafx.geometry.Pos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增修改文章
     *
     * @param post
     */
    public void saveOrUpdate(Post post) {

        Category category = post.getCategory();
        if (category != null) {
            Category _category = categoryService.get(category.getId());
            post.setCategory(_category);
        } else {
            throw new RuntimeException("请选择文章类别");
        }

        if (post.getAttachmentIds() != null) {
            post = postDao.getAttachments(post);
        }

        postDao.saveOrUpdate(post);

    }

    /**
     * 读取单一文章，并增加访问量
     *
     * @param id
     * @return
     */
    public Post get(String id) {

        Post post = postDao.get(id);

        //每访问一次文章，访问量+1
        BigInteger viewCount = post.getViewCount();
        post.setViewCount(viewCount.add(BigInteger.ONE));
        postDao.update(post);
        return post;

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

    private HqlArgs genHqlArgs(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states, String userId) {
        param = StringUtils.trimToEmpty(param);
        userId = StringUtils.trimToEmpty(userId);

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
            if (states.size() == 1) {
                hql += " and p.state = :state";
                args.put("state", states.get(0));
            } else {
                hql += " and p.state in :states";
                args.put("states", states);
            }
        } else {
            hql += " and p.state <> -1";
        }

        if (postType != null && StringUtils.isNotBlank(postType.toString())) {
            hql += " and p.postType=:postType";
            args.put("postType", postType);
        }

        if (StringUtils.isNotBlank(userId)) {
            hql += " and p.creator.id = :userId";
            args.put("userId", userId);
        }


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
    public List<Post> getList(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states, Integer startIndex, Integer rows) {
        HqlArgs hqlArgs = genHqlArgs(postType, param, categoryIds, states, null);
        List<Post> list = postDao.findByNamedParam(hqlArgs.getHql(), startIndex, rows, hqlArgs.getArgs());
        return list;
    }

    public int getListSize(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states) {
        HqlArgs hqlArgs = genHqlArgs(postType, param, categoryIds, states, null);
        List<Post> list = postDao.findByNamedParam(hqlArgs.getHql(), hqlArgs.getArgs());
        return list.size();
    }

    /**
     * 条件查询
     *
     * @param postType
     * @param param
     * @param categoryIds
     * @param states
     * @param userId
     * @return
     */
    public List<Post> getList(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states, String userId) {
        HqlArgs hqlArgs = this.genHqlArgs(postType, param, categoryIds, states, userId);
        String hql = hqlArgs.getHql() + " ORDER BY p.createTime DESC";
        List<Post> byNamedParam = postDao.findByNamedParam(hql, hqlArgs.getArgs());
        return byNamedParam;
    }

    /***
     * 条件查询 获取集合数量
     * @param postType
     * @param param
     * @param categoryIds
     * @param states
     * @param userId
     * @return
     */
    public long getListSize(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states, String userId) {
        HqlArgs hqlArgs = this.genHqlArgs(postType, param, categoryIds, states, userId);
        long count = postDao.getCount(hqlArgs.getHql(), hqlArgs.getArgs());
        return count;
    }
}
