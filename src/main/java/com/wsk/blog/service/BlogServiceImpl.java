package com.wsk.blog.service;

import com.wsk.blog.NotFoundException;
import com.wsk.blog.dao.BlogRepository;
import com.wsk.blog.dao.TypeRepository;
import com.wsk.blog.po.Blog;
import com.wsk.blog.util.MarkdownUtils;
import com.wsk.blog.util.MyBeanUtils;
import com.wsk.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author wsk
 * @date 2020/4/16 9:07
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private TypeRepository typeRepository;

    /**
     * 查询博客
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    /**
     * 根据id查询博客并转换成HTML显示在blog页面
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if(blog == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = blog.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    /**
     * 查询博客列表
     * @param pageable
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            /*Jpa 使用 Specification 实现动态查询*/
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<String>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlogByPublish(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            /*Jpa 使用 Specification 实现动态查询*/
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<String>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                predicates.add(cb.notEqual(root.<Boolean>get("published"),blog.isPublished()));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    @Transactional
    public Page<Blog> listBlogByPublished(Pageable pageable) {
        return blogRepository.findAllByPublished(pageable);
    }

    @Override
    public List<Blog> listBlogByTypeIdAndPublished(Long typeId) {
        return blogRepository.findAllByTypeAndPublished(typeId);
    }


    /**
     * 关联查询，根据tagId查询博客
     * @param tagId
     * @param pageable
     * @return
     */
    @Override
    @Transactional
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    @Transactional
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    /**
     * 查询最新推荐blog
     * @param size
     * @return
     */
    @Override
    @Transactional
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    /**
     * 博客归档
     * @return
     */
    @Override
    @Transactional
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new LinkedHashMap<>();
        for(String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 归档查询blog条数
     * @return
     */
    @Override
    public Long countBlog() {
        return blogRepository.countByPublished();
    }

    /**
     * 新增 更新博客
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    /**
     * 更新博客
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if(b == null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));//过滤掉属性值为空的属性，只copy里面有值的
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    /**
     * 删除博客
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listBlogByType() {
        return blogRepository.findByType();
    }

    @Override
    public Map<String, Long> countByMonth() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,Long> map = new LinkedHashMap<>();
        for(String year : years){
            map.put(year,blogRepository.countByMonth(year));
        }
        return map;
    }

    @Override
    public Map<String, Long> countByTypeAndPublished() {
        List<Long> ids = typeRepository.findTypeId();
        Map<String,Long> map = new LinkedHashMap<>();
        for(Long id: ids){
            String name = typeRepository.findTypeName(id);
            map.put(name,typeRepository.countByTypeAndPublished(id));
        }
        return map;
    }

}
