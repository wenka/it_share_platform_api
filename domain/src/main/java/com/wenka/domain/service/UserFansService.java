package com.wenka.domain.service;

import com.wenka.domain.dao.UserDao;
import com.wenka.domain.dao.UserFansDao;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.User;
import com.wenka.domain.model.UserFans;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户粉丝类
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/10.
 */
@Service
public class UserFansService {

    private static final String RELATION_DIRECTION_ME = "me"; //我关注的
    private static final String RELATION_DIRECTION_FOCUS_ME = "focusMe"; //关注我的

    @Autowired
    private UserFansDao userFansDao;

    @Autowired
    private UserDao userDao;

    /**
     * 保存用户粉丝关系
     */
    public void save(UserFans userFans) {
        User fans = userDao.get(userFans.getFansId());
        User owner = userDao.get(userFans.getOwnerId());
        userFans.setFans(fans);
        userFans.setOwner(owner);
        userFansDao.save(userFans);
    }

    /***
     * 删除关系
     * @param id
     */
    public void delete(String id) {
        this.userFansDao.delete(id);
    }

    private HqlArgs getHqlArgs(String direction, String userId) {
        direction = StringUtils.trimToEmpty(direction);

        Map<String, Object> args = new HashMap<String, Object>();

        String hql = "FROM UserFans uf WHERE 1=1";

        if (StringUtils.isNotBlank(direction)) {
            if (direction.contains(RELATION_DIRECTION_ME)) { //我关注的
                hql += " uf.owner.id = :userId";
            } else if (direction.contains(RELATION_DIRECTION_FOCUS_ME)) { //关注我的
                hql += " uf.fans.id = :userId";
            }
            args.put("userId", userId);
        }

        return new HqlArgs(hql,args);
    }

    /**
     * 查询 我关注的|关注我的 列表集合
     * @param direction
     * @param userId
     * @return
     */
    public List<UserFans> getList(String direction,String userId) {
        HqlArgs hqlArgs = this.getHqlArgs(direction, userId);
        String hql = hqlArgs.getHql() + " ORDER BY uf.createTime DESC";
        return userFansDao.findByNamedParam(hql,hqlArgs.getArgs());
    }

    /***
     * 查询 我关注|关注我的 列表数量
     * @param direction
     * @param userId
     * @return
     */
    public long getListSize(String direction,String userId){
        HqlArgs hqlArgs = this.getHqlArgs(direction, userId);
        return userFansDao.getCount(hqlArgs.getHql(),hqlArgs.getArgs());
    }
}
