package com.wenka.web.controller;

import com.wenka.domain.model.UserFans;
import com.wenka.domain.service.UserFansService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户关系控制器
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/10.
 */
@RestController
@RequestMapping("/userFans")
public class UserFansController extends BaseController {

    @Autowired
    private UserFansService userFansService;

    /**
     * 保存
     *
     * @param userFans
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody UserFans userFans) {
        userFansService.save(userFans);
        this.saveLog(userFans.getOwnerName() + "关注了" + userFans.getFansName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        userFansService.delete(id);
    }

    /**
     * 获取用户关系列表
     *
     * @param direction
     * @param userId
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UserFans> getList(@RequestParam(required = true) String direction,
                                  @RequestParam(required = false) String userId) {
        if (StringUtils.isBlank(userId)) {
            userId = this.currentUserId;
        }
        return userFansService.getList(direction, userId);
    }

    /**
     * 获取关系数量
     *
     * @param direction
     * @param userId
     * @return
     */
    @RequestMapping(value = "/listSize", method = RequestMethod.GET)
    public long getListSize(@RequestParam(required = true) String direction,
                            @RequestParam(required = false) String userId) {
        if (StringUtils.isBlank(userId)) {
            userId = this.currentUserId;
        }
        return userFansService.getListSize(direction, userId);
    }

}
