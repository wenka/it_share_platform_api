package com.wenka.domain.dao;

import com.wenka.domain.model.Attachment;
import com.wenka.domain.model.Post;
import com.wenka.domain.model.PostAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@Repository
public class PostDao extends BaseDao<Post, String> {

    @Autowired
    private AttachmentDao attachmentDao;

    public Post getAttachments(Post post){

        PostAttachment postAttachments=new PostAttachment();
        Attachment attachment = attachmentDao.get(post.getAttachmentIds());
        postAttachments.setAttachment(attachment);
        postAttachments.setOwner(post);

        return post;

    }
}
