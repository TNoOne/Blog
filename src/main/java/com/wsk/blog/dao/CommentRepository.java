package com.wsk.blog.dao;

import com.wsk.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wsk
 * @date 2020/4/24 21:47
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    /**
     * 查询出Comment list，根据blogId 和 parentComment为空的条件
     * @param blogId
     * @param sort
     * @return
     */
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);


    List<Comment> findByBlogNullAndParentCommentNull(Sort sort);

}
