package com.wenka.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
public class HqlArgs {
    private String hql;
    private Map<String, Object> args = new HashMap();

    public HqlArgs() {
    }

    public HqlArgs(String hql, Map<String, Object> args) {
        this.hql = hql;
        this.args = args;
    }

    public String getHql() {
        return this.hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public Map<String, Object> getArgs() {
        return this.args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
