package com.example.demo.controller;

import com.example.demo.service.RegisterService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package: com.example.demo.controller
 * @ClassName: WebController
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@Controller
public class WebController {
//    service只能Autowired，不能Import！！！！！！！！！！！！！！！！
    @Autowired
    RegisterService registerService;
//    登录界面
    @RequestMapping("/login")
    public String index() {
        return "login";
    }
//    登录请求
    @RequestMapping("/tologin")
    public String tologin(String name,String password,Model m) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        try {
            subject.login(token);
            Session session = subject.getSession();
            session.setAttribute("subject", subject);
            return "redirect:operate";

        } catch (AuthenticationException e) {
            m.addAttribute("error", "登录失败");
            return "login";
        }
    }
    //    登录界面
    @RequestMapping("/unAuth")
    public String unanthorized() {
        return "unAuth";
    }
//    注册页面
    @RequestMapping("/register")
    public String registerPage(Model m) {
        return "register";
    }
//    注册账号
    @RequestMapping("/toregister")
    public String register(Model m, @Param("name") String name, @Param("password") String password) {
//        判断用户是否已注册
        if (registerService.hasUser(name)){
            m.addAttribute("error", "用户名已存在");
            return "register";
        }
        try {
            registerService.register(name,password);
            return "login";
        }catch (Exception e){
            m.addAttribute("error", "注册失败，请重试或联系管理员");
            return "register";
        }
    }
    //    操作列表
    @RequestMapping("/operate")
    public String operate() {
        return "operate/operationList";
    }
//    增
    @RequestMapping("/adduser")
    public String add() {
        return "operate/addUser";
    }
//    改
    @RequestMapping("/updateuser")
    public String update() {
        return "operate/updateUser";
    }
}
