package com.wsk.blog.controller;

import com.wsk.blog.po.Blog;
import com.wsk.blog.po.Tag;
import com.wsk.blog.po.Type;
import com.wsk.blog.service.BlogService;
import com.wsk.blog.service.TagService;
import com.wsk.blog.service.TypeService;
import com.wsk.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsk
 * @date 2020/4/22 15:23
 */
@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    /**
     * 跳转到type页面，并显示信息
     * @param pageable
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8,sort = {"createTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @PathVariable Long id,  Model model){
        List<Tag> tags = tagService.listTagTop(1000);
        List<Tag> tags1 = getBlogByTagAndPublished(tags);
        List<Type> types = typeService.listTypeTop(6);
        List<Type> types1 = getBlogPublished(types);
        if(id == -1){
            id = tags.get(0).getId();
        }
        model.addAttribute("types",types1);
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(5));
        model.addAttribute("tags",tags1);
        model.addAttribute("blogCount",blogService.countByMonth());
        model.addAttribute("page",blogService.listBlog(id,pageable));//根据一个tagId查询这个tag下的已发布的blog列表
        model.addAttribute("activeTagId",id);//把id传到前面显示被选中的type
        return "tags";
    }

    /**
     * 获取单个分类下已发布的blogList
     * @param types
     * @return
     */
    public List<Type> getBlogPublished(List<Type> types){
        List<Type> types1 = new ArrayList<>();
        for(Type type : types){
            List<Blog> blogs = type.getBlogs();
            List<Blog> blogsPublished = new ArrayList<>();
            for(Blog blog : blogs){
                if(blog.isPublished()){
                    blogsPublished.add(blog);
                }
            }
            type.setBlogs(blogsPublished);
            types1.add(type);
        }
        return types1;
    }

    /**
     * 获取单个tag下的已发布的blogList
     * @param tags
     * @return
     */
    public List<Tag> getBlogByTagAndPublished(List<Tag> tags){
        List<Tag> tags1 = new ArrayList<>();
        for(Tag tag : tags){
            List<Blog> blogs = tag.getBlogs();
            List<Blog> blogsPublished = new ArrayList<>();
            for(Blog blog : blogs){
                if(blog.isPublished()){
                    blogsPublished.add(blog);
                }
            }
            tag.setBlogs(blogsPublished);
            tags1.add(tag);
        }
        return tags1;
    }


}
