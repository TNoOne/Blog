package com.wsk.blog.controller.admin;
import com.wsk.blog.po.User;
import com.wsk.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
/**
 * @author wsk
 * @date 2020/4/13 14:56
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到后台登录页面
     * @return
     */
    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session,RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if(user != null){
            user.setPassword(null); //不把密码拿到前面去
            session.setAttribute("user",user);
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message","用户名或密码错误");//redirect使用RedirectAttributes传递信息
            return "redirect:/admin";
        }
    }

    /**
     * 注销
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    /**
     * 跳转注册
     * @return
     */
    @GetMapping("/register")
    public String register(){
        return "admin/register";
    }

    /**
     * 注册用户
     * @param user
     * @param attributes
     * @return
     */
    @PostMapping("/registers")
    public String registerUser(User user, RedirectAttributes attributes){
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        user.setType(0);
        User u = userService.saveUser(user);
        if(u != null){
            attributes.addFlashAttribute("message","注册成功，请登录~");
            return "redirect:/admin";
        }else{
            attributes.addFlashAttribute("message","注册失败");
            return "redirect:/admin";
        }
    }

}