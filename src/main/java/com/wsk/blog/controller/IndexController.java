package com.wsk.blog.controller;
import com.wsk.blog.po.Blog;
import com.wsk.blog.po.Tag;
import com.wsk.blog.po.Type;
import com.wsk.blog.service.BlogService;
import com.wsk.blog.service.TagService;
import com.wsk.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
/**
 * @author wsk
 * @date 2020/4/11 17:35
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 跳转首页(显示出所需要的数据)
     * @return
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 8,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<Tag> tags = tagService.listTagTop(10);
        List<Tag> tags1 = getBlogByTagAndPublished(tags);
        List<Type> types = typeService.listTypeTop(6);
        List<Type> types1 = getBlogPublished(types);
        model.addAttribute("page",blogService.listBlogByPublished(pageable));
        model.addAttribute("types",types1);
        model.addAttribute("tags",tags1);
        model.addAttribute("blogCount",blogService.countByMonth());
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(5));
        return "index";
    }

    /**
     * 查询博客功能
     * @param pageable
     * @param query
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){
        List<Tag> tags = tagService.listTagTop(10);
        List<Tag> tags1 = getBlogByTagAndPublished(tags);
        List<Type> types = typeService.listTypeTop(6);
        List<Type> types1 = getBlogPublished(types);
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("types",types1);
        model.addAttribute("tags",tags1);
        model.addAttribute("blogCount",blogService.countByMonth());
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(5));
        model.addAttribute("query",query);
        return "search";
    }

    /**
     * 查看博客详情
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
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