package com.wsk.blog.dao;
import com.wsk.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * @author wsk
 * @date 2020/4/13 16:52
 */
public interface TypeRepository extends JpaRepository<Type,Long> {

    Type findByName(String name);

    /*首页查询type top*/
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);

    @Query("select id from Type ")
    List<Long> findTypeId();

    @Query("select name from Type where id = ?1")
    String findTypeName(Long typeId);

    @Query("select count(b) from Blog b where b.published = true and b.type.id = ?1 ")
    Long countByTypeAndPublished(Long typeId);


}
