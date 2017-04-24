package com.wenka.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public class Pagination<T> implements Serializable {
    private static final long serialVersionUID = 1308226375742109720L;
    public static final int ROWS_PER_PAGE = 20;
    private Integer totalPage = Integer.valueOf(0);
    private Integer page = Integer.valueOf(1);
    private Integer count = Integer.valueOf(0);
    private Integer rows = Integer.valueOf(20);
    private List<T> records = new ArrayList();

    public Pagination(Integer page) {
        if(page != null && page.intValue() >= 1) {
            this.page = page;
        } else {
            this.page = Integer.valueOf(1);
        }

    }

    public Pagination(Integer page, Integer rows) {
        if(page != null && page.intValue() >= 1) {
            this.page = page;
        } else {
            this.page = Integer.valueOf(1);
        }

        if(rows != null && rows.intValue() > 0) {
            this.rows = rows;
        } else {
            this.rows = Integer.valueOf(20);
        }

    }

    public Integer getPage() {
        this.getTotalPage();
        return this.page.intValue() > this.totalPage.intValue()?this.totalPage:this.page;
    }

    public void setPage(Integer page) {
        if(page != null && page.intValue() >= 1) {
            this.page = page;
        } else {
            this.page = Integer.valueOf(1);
        }

    }

    public Integer getTotalPage() {
        if(this.count.intValue() < 1) {
            return Integer.valueOf(0);
        } else {
            this.totalPage = Integer.valueOf(this.count.intValue() / this.rows.intValue());
            if(this.count.intValue() % this.rows.intValue() > 0) {
                this.totalPage = Integer.valueOf(this.totalPage.intValue() + 1);
            }

            return this.totalPage;
        }
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        if(count != null && count.intValue() >= 0) {
            this.count = count;
        } else {
            this.count = Integer.valueOf(0);
        }

    }

    public Integer getRows() {
        return this.rows;
    }

    public void setRows(Integer rows) {
        if(rows != null && rows.intValue() > 0) {
            this.rows = rows;
        } else {
            this.rows = Integer.valueOf(20);
        }

    }

    public List<T> getRecords() {
        return this.records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Integer getStartIdx() {
        return Integer.valueOf(this.rows.intValue() * this.page.intValue() - this.rows.intValue());
    }
}
