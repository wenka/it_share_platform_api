package com.wenka.web.controller;

import com.wenka.commons.util.Pagination;
import com.wenka.domain.model.Log;
import com.wenka.domain.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-15.
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController {

    @Autowired
    private LogService logService;

    /**
     * 保存
     *
     * @param log
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody Log log) {
        logService.save(log.getContext(), this.currentUser);
    }

    /**
     * 获取日志记录
     *
     * @param param
     * @param sdate
     * @param edate
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Pagination list(@RequestParam(required = false) String param,
                           @RequestParam(required = false) Date sdate,
                           @RequestParam(required = false) Date edate,
                           @RequestParam(required = false) List<String> userIds,
                           @RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer rows) {
        Pagination<Log> pagination = new Pagination<Log>(page, rows);
        Integer startIdx = pagination.getStartIdx();
        pagination.setCount(logService.getResultSize(param, sdate, edate, userIds));
        pagination.setRecords(logService.getResultList(param, sdate, edate, startIdx, rows, userIds));
        return pagination;
    }
}
