package com.wsk.blog.dao;
import com.wsk.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author wsk
 * @date 2020/4/16 9:07
 */
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor/*Jpa 实现动态查询的接口*/ {

    /*查询推荐博客*/
    @Query("select b from Blog b where b.recommend = true and b.published = true")
    List<Blog> findTop(Pageable pageable);

    /*用于查询博客功能*/
    @Query("select b from Blog b where b.published = true and (b.title like ?1 or b.content like ?1)")
    Page<Blog> findByQuery(String query,Pageable pageable);

    /*归档 倒叙查询出所有的年份*/
    @Query("select function('date_format',b.createTime,'%Y-%m') as year from Blog b where b.published = true group by function('date_format',b.createTime,'%Y-%m') order by year desc")
    List<String> findGroupYear();

    /*根据年份查询到 blog的list*/
    @Query("select b from Blog b where function('date_format',b.createTime,'%Y-%m') = ?1 and b.published = true")
    List<Blog> findByYear(String year);

    /*更新views*/
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    /**
     * 查找发布的博客
     * @param
     * @return
     */
    @Query("select b from Blog b where b.published = true")
    Page<Blog> findAllByPublished(Pageable pageable);

    @Query("select b from Blog b where b.published = true")
    Page<Blog> findAllByPublished(@Nullable Specification<Blog> var1, Pageable var2);

    @Query("select count(b) from Blog b where b.published = true")
    long countByPublished();

    @Query("select count(b) from Blog b where b.published=true and function('date_format',b.createTime,'%Y-%m') =?1")
    Long countByMonth(String month);

    @Query("select b from Blog b where b.published <> true and b.type.id = ?1")
    List<Blog> findAllByTypeAndPublished(Long typeId);

    @Query("SELECT b from Blog b, Type t where b.type.id = t.id AND b.published = TRUE")
    List<Map<Long,Blog>> findBlogByType();

    @Query(value = "SELECT * FROM t_blog b WHERE b.published = true",nativeQuery = true )
    List<Blog> findByType();

    @Query(value = "SELECT t.*,count(*) FROM t_blog b,t_type t WHERE t.id = b.type_id AND b.published = true GROUP BY b.type_id ",nativeQuery = true)
    List<Blog> findByTag();



}
