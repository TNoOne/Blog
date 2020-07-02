package com.wsk.blog.dao;
import com.wsk.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wsk
 * @date 2020/4/13 14:52
 */
public interface UserRepository extends JpaRepository<User,Long> {

    // 根据用户名+密码查询用户
    User findByUsernameAndPassword(String username,String password);

}
