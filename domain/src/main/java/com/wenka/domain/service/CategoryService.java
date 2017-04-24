package com.wenka.domain.service;

import com.wenka.domain.dao.CategoryDao;
import com.wenka.domain.model.Category;
import com.wenka.domain.model.HqlArgs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    /**
     * 新增或者修改
     *
     * @param category
     */
    public void saveOrUpdate(Category category) {
        this.categoryDao.saveOrUpdate(category);
    }

    /**
     * 标记删除
     *
     * @param id
     */
    public void delete(String id) {
        Category category = this.categoryDao.get(id);
        if (category != null) {
            category.setState(Integer.valueOf(-1));
            this.categoryDao.update(category);
        }
    }

    /**
     * 读取单一栏目
     *
     * @param id
     * @Return Category
     */
    public Category get(String id) {

        return categoryDao.get(id);

    }

    /**
     * 根据条件生成查询hql
     *
     * @param param
     * @param parentId
     * @return
     */
    private HqlArgs genHqlArgs(String param, String parentId) {
        param = StringUtils.trimToEmpty(param);

        Map<String, Object> args = new HashMap<String, Object>();
        String hql = "from Category c where 1=1";

        if (StringUtils.isNotBlank(param)) {
            hql += " and (c.name like :arg or c.remark like :arg)";
            args.put("arg", "%" + param + "%");
        }
        if (StringUtils.isNotBlank(parentId)) {
            hql += " and c.parent.id = :parentId";
            args.put("parentId", parentId);
        }

        return new HqlArgs(hql, args);

    }

    /**
     * 根据入参查询结果数量
     *
     * @param param
     * @return
     */
    public int getResultSize(String param, String parentId) {
        HqlArgs hqlArgs = genHqlArgs(param, parentId);
        return categoryDao.findByNamedParam(hqlArgs.getHql(), hqlArgs.getArgs()).size();
    }

    /**
     * 根据入参查询结果集
     *
     * @param param
     * @param parentId
     * @param startIndex
     * @param length
     * @return
     */
    public List<Category> getResultInfos(String param, String parentId, Integer startIndex, Integer length) {
        HqlArgs hqlArgs = genHqlArgs(param, parentId);
        String hql = "select c " + hqlArgs.getHql() + " order by c.sort desc";
        return categoryDao.findByNamedParam(hql, startIndex, length, hqlArgs.getArgs());
    }
}
