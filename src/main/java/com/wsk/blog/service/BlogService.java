package com.wsk.blog.service;
import com.wsk.blog.po.Blog;
import com.wsk.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author wsk
 * @date 2020/4/16 9:04
 */
public interface BlogService {

    Blog getBlog(Long id);

    /*前端显示 获取并转换 显示在blog页面*/
    Blog getAndConvert(Long id);

    /*分页查询博客列表，还要传Blog*/
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    /*分类查询publish的blog*/
    Page<Blog> listBlogByPublish(Pageable pageable,BlogQuery blogQuery);

    /*首页查询博客列表，不需要blog条件*/
    Page<Blog> listBlogByPublished(Pageable pageable);


    List<Blog> listBlogByTypeIdAndPublished(Long typeId);


    /*根据tagId查询博客，tag页面显示*/
    Page<Blog> listBlog(Long tagId,Pageable pageable);

    /*用于前端展示模块查询功能，query查询条件*/
    Page<Blog> listBlog(String query,Pageable pageable);

    /*首页推荐blog top*/
    List<Blog> listRecommendBlogTop(Integer size);

    /*根据年份查询到blog 放到String,List<Blog>的map中*/
    Map<String,List<Blog>> archiveBlog();

    /*归档查询出博客的条数*/
    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    /*-------------------------------------*/

    List<Blog> listBlogByType();

    Map<String,Long> countByMonth();

    Map<String,Long> countByTypeAndPublished();
}
