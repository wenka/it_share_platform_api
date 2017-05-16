package com.wenka.domain.dao;

import com.wenka.domain.model.Category;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Repository
public class CategoryDao extends BaseDao<Category, String> {
    /**
     * 获取所有栏目集合
     *
     * @return
     */
    public List<Map<String, Object>> getPubCategoryList(Category.CategoryType categoryType) {
        String sql = "SELECT\n" +
                "  group_concat(category.ID) AS id,\n" +
                "  category.name_ AS name\n" +
                "FROM category WHERE state=1\n" +
                " AND category_type = :categoryType\n" +
                "  GROUP BY category.name_\n" +
                "ORDER BY create_time DESC";
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("categoryType",categoryType.toString());
        List<Map<String, Object>> byNamedParamSQL = this.findByNamedParamSQL(sql, args);
        return byNamedParamSQL;
    }
}
