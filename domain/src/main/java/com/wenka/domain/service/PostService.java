package com.wenka.domain.service;

import com.wenka.domain.dao.PostDao;
import com.wenka.domain.model.Category;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.Post;
import com.wenka.domain.model.UserDynamic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LogService logService;

    @Autowired
    private PostTagService postTagService;

    @Autowired
    private UserDynamicService userDynamicService;

    @Value("${limitContextLength}")
    private String limitContextLength;

    @Value("${integral.rate}")
    private String integralRate;

    @Autowired
    private UserService userService;

    /**
     * 新增修改文章
     *
     * @param post
     */
    public void saveOrUpdate(Post post) {

        if (post.getPostType() != Post.PostType.评论) {
            Category category = post.getCategory();
            if (category != null) {
                Category _category = categoryService.get(category.getId());
                post.setCategory(_category);
            } else {
                throw new RuntimeException("请选择文章类别");
            }
        } else if (post.getPostType() == Post.PostType.评论) {
            Post parent = post.getParent();
            if (parent != null) {
                Post _post = postDao.get(parent.getId());
                post.setParent(_post);

                //保存文章主人的动态
                UserDynamic userDynamic = new UserDynamic();
                userDynamic.setOwner(_post.getCreator());
                userDynamic.setPost(_post);
                userDynamic.setMsg(post.getCreatorName() + "评论了您的" + post.getPostType());
                userDynamicService.save(userDynamic);
            }
        }

        if (post.getAttachmentIds() != null) {
            post = postDao.getAttachments(post);
        }
        postDao.saveOrUpdate(post);
        postTagService.save(post);

        if (post.getPostType() == Post.PostType.博客 || post.getPostType() == Post.PostType.头条) {
            this.updateIntegral(post);
        }

    }

    /**
     * 计算用户积分
     *
     * @param post
     */
    private void updateIntegral(Post post) {
        String content = post.getContent();
        if (content != null) {
            BigInteger contextlength = BigInteger.valueOf(content.length());
            BigInteger limitLength = BigInteger.valueOf(Integer.valueOf(limitContextLength));
            BigInteger rate = BigInteger.valueOf(Integer.valueOf(integralRate));
            BigInteger num = contextlength.divide(limitLength).multiply(rate);
            this.userService.updateUserIntegral(post.getCreatorId(), num);
        }
    }

    /**
     * 读取单一文章，并增加访问量
     *
     * @param id
     * @return
     */
    public Post get(String id) {

        Post post = postDao.get(id);
        return post;

    }

    /***
     * 增加阅读量
     * @param currentUserId
     * @param id
     */
    public void updateViwCount(String id, String currentUserId) {
        Post post = postDao.get(id);
        if (post != null) {
            String creatorId = post.getCreatorId();
            if (!creatorId.equals(currentUserId)) {
                BigInteger viewCount = post.getViewCount();
                post.setViewCount(viewCount.add(BigInteger.ONE));
                postDao.update(post);
            }
        }
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
        logService.save(post.getPostType().toString() + "：[" + post.getTitle() + "]删除成功", post.getCreator());
    }

    private HqlArgs genHqlArgs(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states, String userId) {
        param = StringUtils.trimToEmpty(param);
        userId = StringUtils.trimToEmpty(userId);

        Map<String, Object> args = new HashMap<String, Object>();
        String hql = "from Post p where 1=1";

        if (StringUtils.isNotBlank(param)) {
            hql += " and (p.title like :arg or p.subTitle like :arg or p.category.name like :arg)";
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

    /**
     * 按照阅读量排序
     *
     * @param postType
     * @param param
     * @param categoryIds
     * @param states
     * @param userId
     * @return
     */
    public List<Post> getListByViewCount(Post.PostType postType, String param, List<String> categoryIds, List<Integer> states, String userId) {
        HqlArgs hqlArgs = this.genHqlArgs(postType, param, categoryIds, states, userId);
        String hql = hqlArgs.getHql() + " ORDER BY p.viewCount DESC, p.createTime DESC";
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

    /**
     * 查询热门作者
     *
     * @return
     */
    public List<Map<String, Object>> getPopularAuthor() {
        List<Map<String, Object>> popularAuthor = this.postDao.getPopularAuthor();
        return popularAuthor;
    }

    /**
     * 采纳答案
     */
    public void updateAdoption(String id) {
        Post post = this.postDao.get(id);
        post.setAdoption(true);
        this.postDao.update(post);
        if (post.getPostType() == Post.PostType.评论){
            UserDynamic userDynamic = new UserDynamic();
            userDynamic.setMsg(post.getTagNames() + "评论采纳成功");
            userDynamic.setPost(post);
            userDynamic.setOwner(post.getCreator());
            this.userDynamicService.save(userDynamic);
        }

        if (post.getParentId() != null){
            this.updateAdoption(post.getParentId());
            logService.save(post.getPostType().toString() + "：[" + post.getTitle() + "]已采纳",post.getParent().getCreator());
        }
    }
}
