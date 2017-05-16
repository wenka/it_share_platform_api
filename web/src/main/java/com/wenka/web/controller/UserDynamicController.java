package com.wenka.web.controller;

import com.wenka.domain.model.UserDynamic;
import com.wenka.domain.service.UserDynamicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 用户动态控制器
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/16.
 */
@RestController
@RequestMapping("/userDynamic")
public class UserDynamicController extends BaseController {

    @Autowired
    private UserDynamicService userDynamicService;

    /**
     * 保存动态
     *
     * @param userDynamic
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    private void save(@RequestBody UserDynamic userDynamic) {
        if (userDynamic.getOwner() == null) {
            userDynamic.setOwner(this.currentUser);
        }
        userDynamicService.save(userDynamic);
    }

    /**
     * 标记为已读 state:1   删除 state:-1
     *
     * @param id
     * @param state
     */
    @RequestMapping(value = "/{id}/{state}", method = RequestMethod.POST)
    public void updateState(@PathVariable String id,
                            @PathVariable Integer state) {
        this.userDynamicService.updateState(id, state);
    }

    /**
     * 获取某一个动态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDynamic get(@PathVariable String id) {
        return this.userDynamicService.get(id);
    }

    /**
     * 获取未读消息
     *
     * @param userId
     * @param param
     * @return
     */
    @RequestMapping(value = "/unread",method = RequestMethod.GET)
    public long getUnreadMsg(@RequestParam(required = false) String userId,
                             @RequestParam(required = false) String param) {
        if (StringUtils.isNotBlank(StringUtils.trimToNull(userId))) {
            userId = this.currentUserId;
        }

        List<Integer> states = new LinkedList<Integer>();
        states.add(Integer.valueOf(0));
        long unreadMsg = this.userDynamicService.getUnreadMsg(userId, states, param);
        return unreadMsg;
    }

    /**
     * 获取动态列表
     *
     * @param userId
     * @param states
     * @param param
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<UserDynamic> getList(@RequestParam(required = false) String userId,
                                     @RequestParam(required = false) List<Integer> states,
                                     @RequestParam(required = false) String param) {
        if (StringUtils.isNotBlank(StringUtils.trimToNull(userId))) {
            userId = this.currentUserId;
        }
        List<UserDynamic> list = this.userDynamicService.list(userId, states, param);
        return list;
    }

}
