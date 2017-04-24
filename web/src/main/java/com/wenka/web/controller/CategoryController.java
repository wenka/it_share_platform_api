package com.wenka.web.controller;

import com.wenka.commons.util.Pagination;
import com.wenka.domain.model.Category;
import com.wenka.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-15.
 */
@RestController
@Repository("/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 修改或者保存
     *
     * @param category
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveOrUpdateCategory(@RequestBody Category category) {
        categoryService.saveOrUpdate(category);
    }

    /**
     * 删除栏目
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
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
                           @RequestParam(required = false) Integer length) {
        Pagination<Category> pagination = new Pagination<Category>(page, length);
        Integer startIdx = pagination.getStartIdx();
        pagination.setCount(categoryService.getResultSize(param, parentId));
        pagination.setRecords(categoryService.getResultInfos(param, parentId, startIdx, length));
        return pagination;
    }
}
