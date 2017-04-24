package com.wenka.domain.service;

import com.wenka.domain.dao.LeaveMessageDao;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.LeaveMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Service
public class LeaveMessageService {

    @Autowired
    private LeaveMessageDao leaveMessageDao;

    private HqlArgs getHqlArgs(String param) {
        param = StringUtils.trimToEmpty(param);

        HashMap<String, Object> args = new HashMap<String, Object>();

        String hql = "FROM LeaveMessage msg WHERE 1=1";

        if (StringUtils.isNotBlank(param)) {
            hql += " AND (msg.name LIKE :param OR msg.address LIKE :param OR msg.productLine LIKE :param OR msg.companyName LIKE :param OR msg.remark LIKE :param)";
            args.put("param", "%" + param + "%");
        }

        hql += " AND msg.state <> -1";
        hql += " ORDER BY msg.createTime DESC";

        return new HqlArgs(hql, args);
    }

    /**
     * 查看所有的留言信息
     *
     * @param param
     * @param startIndex
     * @param rows
     * @return
     */
    public List<LeaveMessage> getLeaveMessageList(String param, Integer startIndex, Integer rows) {

        HqlArgs hqlArgs = getHqlArgs(param);

        List<LeaveMessage> byNamedParam = leaveMessageDao.findByNamedParam(hqlArgs.getHql(), startIndex, rows, hqlArgs.getArgs());

        return byNamedParam;
    }


    /**
     * 获取总记录数
     *
     * @param param
     * @return
     */
    public int getLeaveMessageList(String param) {

        int count = 0;

        HqlArgs hqlArgs = getHqlArgs(param);

        List<LeaveMessage> byNamedParam = leaveMessageDao.findByNamedParam(hqlArgs.getHql(), hqlArgs.getArgs());

        if (byNamedParam != null) {
            count = byNamedParam.size();
        }

        return count;
    }

    /**
     * 增加留言
     *
     * @param leaveMessage
     */
    public void saveLeaveMessage(LeaveMessage leaveMessage) {
        leaveMessageDao.save(leaveMessage);
    }
}
