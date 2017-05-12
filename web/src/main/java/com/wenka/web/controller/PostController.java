package com.wenka.web.controller;

import com.wenka.commons.util.Pagination;
import com.wenka.domain.model.Post;
import com.wenka.domain.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-15.
 */
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    /**
     * 保存或者修改
     *
     * @param post
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveOrUpdatePost(@RequestBody Post post) {
        post.setCreator(this.currentUser);
        post.setAuthor(this.currentUser.getName());
        postService.saveOrUpdate(post);
        this.saveLog(post.getPostType().toString() + "：[" + post.getTitle() + "]保存成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePost(@PathVariable String id) {
        postService.delete(id);
    }

    /**
     * 读取单一文章
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Post get(@PathVariable String id) {
        //增加阅读量
        postService.updateViwCount(id,this.currentUserId);
        return postService.get(id);
    }

    /**
     * 获取集合
     *
     * @param param
     * @param categoryIds
     * @param states
     * @param page
     * @param length
     * @retur
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Pagination list(@RequestParam(required = true) Post.PostType postType,
                           @RequestParam(required = false) String param,
                           @RequestParam(required = false) List<String> categoryIds,
                           @RequestParam(required = false) List<Integer> states,
                           @RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer length) {
        Pagination<Post> pagination = new Pagination<Post>(page, length);
        Integer startIdx = pagination.getStartIdx();
        pagination.setCount(postService.getListSize(postType, param, categoryIds, states));
        pagination.setRecords(postService.getList(postType, param, categoryIds, states, startIdx, length));
        return pagination;
    }

    /**
     * 获取集合
     *
     * @param userId
     * @param param
     * @param postType
     * @param categoryIds
     * @param states
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public List<Post> getList(@RequestParam(required = false) String userId,
                              @RequestParam(required = false) String param,
                              @RequestParam(required = false) Post.PostType postType,
                              @RequestParam(required = false) List<String> categoryIds,
                              @RequestParam(required = false) List<Integer> states) {
        if (StringUtils.isBlank(userId)) {
            userId = this.currentUserId;
        }
        List<Post> list = postService.getList(postType, param, categoryIds, states, userId);
        return list;
    }

    /***
     * 获取集合数量
     * @param userId
     * @param param
     * @param postType
     * @param categoryIds
     * @param states
     * @return
     */
    @RequestMapping(value = "/getListSize", method = RequestMethod.GET)
    public long getListSize(@RequestParam(required = false) String userId,
                            @RequestParam(required = false) String param,
                            @RequestParam(required = false) Post.PostType postType,
                            @RequestParam(required = false) List<String> categoryIds,
                            @RequestParam(required = false) List<Integer> states) {
        if (StringUtils.isBlank(userId)) {
            userId = this.currentUserId;
        }
        long listSize = postService.getListSize(postType, param, categoryIds, states, userId);
        return listSize;
    }
}
