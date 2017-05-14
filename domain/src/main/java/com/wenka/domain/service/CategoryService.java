package com.wenka.domain.service;

import com.wenka.domain.dao.CategoryDao;
import com.wenka.domain.dao.UserDao;
import com.wenka.domain.model.Category;
import com.wenka.domain.model.HqlArgs;
import com.wenka.domain.model.User;
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

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    /**
     * 新增或者修改
     *
     * @param category
     */
    public void saveOrUpdate(Category category) {
        User creator = category.getCreator();
        if (creator == null) {
            throw new RuntimeException("不能识别当前用户");
        }
        User user = userService.get(creator.getId());
        if (user == null) {
            throw new RuntimeException("不能识别当前用户");
        }
        category.setCreator(user);
        this.categoryDao.saveOrUpdate(category);
    }

    /**
     * 更改状态
     * 标记删除/设为禁用/启用
     *
     * @param id
     */
    public void delete(String id, Integer state) {
        Category category = this.categoryDao.get(id);
        if (category != null) {
            category.setState(state);
            this.categoryDao.update(category);
            String info = "";
            switch (state.intValue()) {
                case -1:
                    info = "删除";
                    break;
                case 0:
                    info = "禁用";
                    break;
                case 1:
                    info = "启用";
                    break;
            }
            logService.save("类别：[" + category.getName() + "]" + info + "成功", category.getCreator());
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
        String hql = "select c " + hqlArgs.getHql() + " order by c.sort desc,createTime desc";
        return categoryDao.findByNamedParam(hql, startIndex, length, hqlArgs.getArgs());
    }

    /**
     * 通过用户查询所拥有的类别
     *
     * @param userId
     * @return
     */
    public List<Category> getCategoryListByUser(String userId) {
        String hql = "FROM Category WHERE creator.id = ? AND state <> -1 ORDER BY createTime DESC";
        List<Category> categories = categoryDao.find(hql, userId);
        return categories;
    }

    /**
     * 获取栏目集合
     * tips:若栏目名相同则合并
     *
     * @return
     */
    public List<Map<String, Object>> getPubCategoryList() {
        List<Map<String, Object>> pubCategoryList = categoryDao.getPubCategoryList();
        return pubCategoryList;
    }
}
