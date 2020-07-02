package com.wsk.blog.service;

import com.wsk.blog.po.Comment;

import java.util.List;

/**
 * @author wsk
 * @date 2020/4/24 21:45
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    List<Comment> listCommentByNull();
}
