package com.wenka.domain.service;

import com.wenka.domain.dao.UsetrDynamicDao;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.Post;
import com.wenka.domain.model.User;
import com.wenka.domain.model.UserDynamic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户动态
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/15.
 */
@Service
public class UserDynamicService {

    @Autowired
    private UsetrDynamicDao userDynamicDao;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /***
     * 保存
     * @param userDynamic
     */
    public void save(UserDynamic userDynamic) {
        Post post = userDynamic.getPost();
        User owner = userDynamic.getOwner();

        if (post != null){
            post = postService.get(post.getId());
        }

        if (owner != null){
            owner = userService.get(owner.getId());
        }

        userDynamic.setPost(post);
        userDynamic.setOwner(owner);
        userDynamicDao.save(userDynamic);
    }

    /***
     * 更改状态 0：未读  1：已读  -1：删除
     * @param id
     * @param state
     */
    public void updateState(String id, Integer state) {
        UserDynamic userDynamic = this.get(id);
        if (userDynamic != null) {
            userDynamic.setState(state);
            userDynamicDao.update(userDynamic);
        }
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public UserDynamic get(String id) {
        return userDynamicDao.get(id);
    }

    /**
     * 自动生成sql
     *
     * @param userId
     * @param states
     * @param param
     * @return
     */
    private HqlArgs getHqlArgs(String userId, List<Integer> states, String param) {
        param = StringUtils.trimToNull(param);

        Map<String, Object> args = new HashMap<String, Object>();

        String hql = " FROM UserDynamic ud WHERE 1=1 AND ud.owner.id = :userId";
        args.put("userId", userId);

        if (states != null && states.size() > 0) {
            if (states.size() == 1) {
                hql += " AND ud.state = :state";
                args.put("state", states.get(0));
            } else {
                hql += " AND ud.state in :states";
                args.put("states", states);
            }
        } else {
            hql += " AND ud.state <> -1";
        }

        if (StringUtils.isNotBlank(param)) {
            hql += " AND(ud.msg LIKE :param)";
            args.put("param", param);
        }

        return new HqlArgs(hql, args);
    }

    /**
     * 查询消息记录
     *
     * @param userId
     * @param states
     * @param param
     * @return
     */
    public List<UserDynamic> list(String userId, List<Integer> states, String param) {
        HqlArgs hqlArgs = this.getHqlArgs(userId, states, param);
        String hql = hqlArgs.getHql() + " ORDER BY ud.createTime DESC";
        List<UserDynamic> byNamedParam = this.userDynamicDao.findByNamedParam(hql, hqlArgs.getArgs());
        return byNamedParam;
    }

    /**
     * 统计消息个数
     *
     * @param userId
     * @param states
     * @param param
     * @return
     */
    public long getUnreadMsg(String userId, List<Integer> states, String param) {
        HqlArgs hqlArgs = this.getHqlArgs(userId, states, param);
        long count = this.userDynamicDao.getCount(hqlArgs.getHql(), hqlArgs.getArgs());
        return count;
    }
}
