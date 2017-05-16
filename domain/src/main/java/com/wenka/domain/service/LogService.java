package com.wenka.domain.service;

import com.wenka.commons.util.Convertor;
import com.wenka.domain.dao.LogDao;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.Log;
import com.wenka.domain.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Service
public class LogService {

    @Autowired
    private LogDao logDao;

    /**
     * 保存信息
     *
     * @param msg
     * @param currentUser
     */
    public void save(String msg, User currentUser) {
        Log log = new Log();
        log.setUser(currentUser);
        log.setContext(msg);
        this.logDao.save(log);
    }

    private HqlArgs getHqlArgs(String param, Date sdate, Date edate, List<String> userIds) {
        HashMap args = new HashMap();
        String hql = "from Log l where 1=1";
        if(sdate != null) {
            hql = hql + " and l.createTime >= :sdate";
            args.put("sdate", Convertor.getMinTime(sdate));
        }

        if(edate != null) {
            hql = hql + " and l.createTime <= :edate";
            args.put("edate", Convertor.getMaxTime(edate));
        }

        param = StringUtils.trimToEmpty(param);
        if(StringUtils.isNotBlank(param)) {
            hql = hql + " and l.context like :event";
            args.put("event", "%" + param + "%");
        }

        if(userIds != null && userIds.size() > 0) {
            if(userIds.size() == 1) {
                hql = hql + " and l.user.id=:organIds";
                args.put("organIds", userIds.get(0));
            } else {
                hql = hql + " and l.user.id in :organIds";
                args.put("organIds", userIds);
            }
        }

        HqlArgs result = new HqlArgs();
        result.setHql(hql);
        result.setArgs(args);
        return result;
    }

    public Integer getResultSize(String param, Date sdate, Date edate, List<String> organIds) {
        HqlArgs hqlArgs = this.getHqlArgs(param, sdate, edate, organIds);
        String hql = "select count(1) " + hqlArgs.getHql();
        return this.logDao.getResultSize(hql, hqlArgs.getArgs());
    }

    public List<Log> getResultList(String param, Date sdate, Date edate, Integer startIdx, Integer length, List<String> organIds) {
        HqlArgs hqlArgs = this.getHqlArgs(param, sdate, edate, organIds);
        String hql = hqlArgs.getHql() + " order by l.createTime desc";
        return this.logDao.findByNamedParam(hql, startIdx, length, hqlArgs.getArgs());
    }

    public List<Log> getLogList(String param, Date sdate, Date edate,List<String> userIds){
        HqlArgs hqlArgs = this.getHqlArgs(param, sdate, edate, userIds);
        String hql = hqlArgs.getHql() + " order by l.createTime desc";
        return this.logDao.findByNamedParam(hql, hqlArgs.getArgs());
    }
}
