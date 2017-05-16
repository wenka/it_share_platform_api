package com.wenka.web.controller;

import com.wenka.commons.util.Pagination;
import com.wenka.domain.model.Category;
import com.wenka.domain.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-15.
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 修改或者保存
     *
     * @param category
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Category saveOrUpdateCategory(@RequestBody Category category) {
        categoryService.saveOrUpdate(category);
        this.saveLog("类别：[" + category.getName() + "]保存成功");
        return category;
    }

    /**
     * 更改状态
     * 标记删除/设为禁用/启用
     *
     * @param id
     */
    @RequestMapping(value = "/{id}/{state}", method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable("id") String id,@PathVariable("state")Integer state) {
        categoryService.delete(id,state);
    }

    /**
     * 获取栏目
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public Category get(@PathVariable String id) {
        return categoryService.get(id);
    }

    /**
     * 获取栏目集合
     *
     * @param param
     * @param parentId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Pagination list(@RequestParam(required = false) String param,
                           @RequestParam(required = false) String parentId,
                           @RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer length,
                           @RequestParam(required = false) Category.CategoryType categoryType ){
        Pagination<Category> pagination = new Pagination<Category>(page, length);
        Integer startIdx = pagination.getStartIdx();
        pagination.setCount(categoryService.getResultSize(param, parentId,categoryType));
        pagination.setRecords(categoryService.getResultInfos(param, parentId, startIdx, length,categoryType));
        return pagination;
    }

    /**
     * 通过用户查看其类别
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getByUser",method = RequestMethod.GET)
    public List<Category> getListByUser(@RequestParam(required = false) String userId,
                                        @RequestParam(required = false) Category.CategoryType categoryType) {
        userId = StringUtils.trimToEmpty(userId);
        if (StringUtils.isBlank(userId)){
            userId = this.currentUserId;
        }
        if (categoryType == null){
            categoryType = Category.CategoryType.文章类别;
        }
        List<Category> categoryListByUser = categoryService.getCategoryListByUser(userId,categoryType);
        return categoryListByUser;
    }
}
