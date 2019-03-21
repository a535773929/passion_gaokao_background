package com.example.demo.controller;

import com.example.demo.service.RegisterService;
import com.example.demo.util.CaptchaUtil;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
//    Session中图片验证码值的键名
    public static final String KEY_CAPTCHA = "KEY_CAPTCHA";
//    登录界面
    @RequestMapping("/login")
    public String index() {
        return "login";
    }
//    登录请求
    @RequestMapping("/tologin")
    public String tologin(String name,String password,String captcha,Model m) {
//        先判断验证码是否正确
        String sessionCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(KEY_CAPTCHA);
        if (null == captcha || !captcha.equalsIgnoreCase(sessionCaptcha)) {
            m.addAttribute("error", "验证码错误！");
            return "login";
        }
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
//    获取验证码图片
@RequestMapping("/Captcha.jpg")
public void getCaptcha(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    // 设置相应类型,告诉浏览器输出的内容为图片
    response.setContentType("image/jpeg");
    // 不缓存此内容
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expire", 0);
    try {

//        HttpSession session = request.getSession();
        Session session = SecurityUtils.getSubject().getSession();

        CaptchaUtil tool = new CaptchaUtil();
        StringBuffer code = new StringBuffer();
        BufferedImage image = tool.genRandomCodeImage(code);
        session.removeAttribute(KEY_CAPTCHA);
        session.setAttribute(KEY_CAPTCHA, code.toString());

        // 将内存中的图片通过流动形式输出到客户端
        ImageIO.write(image, "JPEG", response.getOutputStream());

    } catch (Exception e) {
        e.printStackTrace();}
    }
}
