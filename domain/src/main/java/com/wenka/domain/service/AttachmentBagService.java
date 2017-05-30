package com.wenka.domain.service;

import com.wenka.domain.dao.AttachmentBagDao;
import com.wenka.domain.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/17.
 */
@Service
public class AttachmentBagService {

    @Autowired
    private AttachmentBagDao attachmentBagDao;

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存
     *
     * @param attachmentBag
     */
    public void save(AttachmentBag attachmentBag) {
        User creator = attachmentBag.getCreator();
        User user = userService.get(creator.getId());

        Attachment attachment = attachmentBag.getAttachment();
        if (attachment != null) {
            attachment = attachmentService.get(attachment.getId());
        } else {
            throw new RuntimeException("附件无效");
        }

        Category category = attachmentBag.getCategory();
        if (category != null) {
            category = categoryService.get(category.getId());
        } else {
            throw new RuntimeException("类别出错了");
        }

        attachmentBag.setCreator(user);
        attachmentBag.setAttachment(attachment);
        attachmentBag.setCategory(category);

        this.attachmentBagDao.save(attachmentBag);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) {
        AttachmentBag attachmentBag = this.attachmentBagDao.get(id);
        if (attachmentBag != null) {
            attachmentBag.setState(Integer.valueOf(-1));
            attachmentBagDao.update(attachmentBag);
        }
    }

    private HqlArgs getHqlArgs(String param, AttachmentBag.FileType fileType, String userId, List<String> categoryIds) {
        param = StringUtils.trimToEmpty(param);
        userId = StringUtils.trimToEmpty(userId);

        Map<String, Object> args = new HashMap<String, Object>();

        String hql = "FROM AttachmentBag ab WHERE 1=1 AND ab.state <> -1";

        if (StringUtils.isNotBlank(param)) {
            hql += " AND (ab.category.name LIKE :param OR ab.remark LIKE :param)";
            args.put("param", param);
        }

        if (StringUtils.isNotBlank(userId)) {
            hql += " AND ab.creator.id = :userId";
            args.put("userId", userId);
        }

        if (categoryIds != null && categoryIds.size() > 0) {
            if (categoryIds.size() == 1) {
                hql += "ab.category.id = :categoryId";
                args.put("categoryId", categoryIds.get(0));
            } else {
                hql += "ab.category.id in :categoryIds";
                args.put("categoryIds", categoryIds);
            }
        }


        if (fileType != null) {
            hql += " AND ab.fileType = :fileType";
            args.put("fileType", fileType);
        }

        return new HqlArgs(hql, args);

    }

    public List<AttachmentBag> list(String param, AttachmentBag.FileType fileType, String userId, List<String> categoryIds) {
        HqlArgs hqlArgs = this.getHqlArgs(param, fileType, userId, categoryIds);
        String hql = hqlArgs.getHql() + " ORDER BY ab.createTime DESC";
        return this.attachmentBagDao.findByNamedParam(hql, hqlArgs.getArgs());
    }

    public long listSize(String param, AttachmentBag.FileType fileType, String userId, List<String> categoryIds) {
        HqlArgs hqlArgs = this.getHqlArgs(param, fileType, userId, categoryIds);
        return this.attachmentBagDao.getCount(hqlArgs.getHql(), hqlArgs.getArgs());
    }
}
