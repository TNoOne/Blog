package com.wsk.blog.service;

import com.wsk.blog.po.User;

/**
 * @author wsk
 * @date 2020/4/13 14:50
 */
public interface UserService {

    User checkUser(String username, String password);

    User saveUser(User user);
}
