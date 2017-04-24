package com.wenka.web.controller;

import com.wenka.commons.util.Pagination;
import com.wenka.commons.web.AuthNotRequired;
import com.wenka.domain.model.LeaveMessage;
import com.wenka.domain.service.LeaveMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@RestController
@RequestMapping("/leaveMessage")
public class LeaveMessageController extends BaseController {

    @Autowired
    private LeaveMessageService leaveMessageService;

    /**
     * 查看留言
     *
     * @param param
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Pagination get(@RequestParam(required = false) String param,
                          @RequestParam(required = false) Integer page,
                          @RequestParam(required = false) Integer rows) {
        Pagination<LeaveMessage> pagination = new Pagination<LeaveMessage>(page, rows);
        Integer startIdx = pagination.getStartIdx();
        pagination.setCount(leaveMessageService.getLeaveMessageList(param));
        pagination.setRecords(leaveMessageService.getLeaveMessageList(param, startIdx, rows));

        return pagination;
    }


    /**
     * 保存留言
     *
     * @param leaveMessage
     */
    @AuthNotRequired
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveLeaveMessage(@RequestBody LeaveMessage leaveMessage) {
        leaveMessageService.saveLeaveMessage(leaveMessage);
    }
}
