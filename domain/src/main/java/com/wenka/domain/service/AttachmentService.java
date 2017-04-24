package com.wenka.domain.service;

import com.wenka.domain.dao.AttachmentDao;
import com.wenka.domain.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 附件
 *
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Service
public class AttachmentService {

    @Autowired
    private AttachmentDao attachmentDao;

    /**
     * 获取附件
     *
     * @param id
     * @return
     */
    public Attachment get(String id) {
        return attachmentDao.get(id);
    }

    /**
     * 保存
     *
     * @param attachment
     */
    public void save(Attachment attachment) {
        this.attachmentDao.save(attachment);
    }
}
