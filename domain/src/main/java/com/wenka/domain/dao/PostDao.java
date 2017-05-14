package com.wenka.domain.dao;

import com.wenka.domain.model.Attachment;
import com.wenka.domain.model.Post;
import com.wenka.domain.model.PostAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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


    /**
     * 查询热门作者
     *
     * @return
     */
    public List<Map<String, Object>> getPopularAuthor() {
        String sql = "SELECT\n" +
                "    sum(post.view_count) AS view_count,\n" +
                "    users.ID,\n" +
                "    users.name_\n" +
                "  FROM post\n" +
                "    LEFT JOIN users ON post.creator_ID = users.ID\n" +
                "  GROUP BY users.ID, users.name_\n" +
                "  ORDER BY view_count DESC";
        List<Map<String, Object>> byNamedParamSQL = this.findByNamedParamSQL(sql, null);
        return byNamedParamSQL;
    }
}
