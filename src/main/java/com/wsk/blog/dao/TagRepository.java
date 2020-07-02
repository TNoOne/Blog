package com.wsk.blog.dao;
import com.wsk.blog.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wsk
 * @date 2020/4/15 22:09
 */

public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag getTagByName(String name);

    /*查询首页tag*/
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);

}
