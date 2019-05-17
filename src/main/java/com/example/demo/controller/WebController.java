package com.example.demo.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.service.RegisterService;
import com.example.demo.util.CaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Package: com.example.demo.controller
 * @ClassName: WebController
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@RestController
public class WebController {
    //    service只能Autowired，不能Import！！！！！！！！！！！！！！！！
    @Autowired
    RegisterService registerService;
    //    Session中图片验证码值的键名
    public static final String KEY_CAPTCHA = "KEY_CAPTCHA";

    //    登录请求
    @RequestMapping("/tologin")
    public JSONObject tologin(String username,String password,String captcha) {
        JSONObject result = JSONUtil.createObj();
//        先判断验证码是否正确
        String sessionCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(KEY_CAPTCHA);
        if (null == captcha || !captcha.equalsIgnoreCase(sessionCaptcha)) {
            result.put("msg","验证码错误");
            result.put("status",401);
            return result;
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            Session session = subject.getSession();
            session.setAttribute("subject", subject);
            result.put("Token",subject.getSession().getId());
            result.put("msg","登录成功");
            result.put("status",200);
            return result;
        } catch (AuthenticationException e) {
            result.put("Token",null);
            result.put("msg","用户名or密码错误");
            result.put("status",401);
            return result;
        }catch (Exception e){
            result.put("Token",null);
            result.put("msg","未知错误");
            result.put("status",500);
            return result;
        }
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
    @RequestMapping("/logoutSuccess")
    public JSONObject logout(){
        JSONObject result = JSONUtil.createObj();
        result.put("msg","退出登陆");
        result.put("status",300);
        return result;
    }
    @RequestMapping("/unAuth")
    public JSONObject unAuth(){
        JSONObject result = JSONUtil.createObj();
        result.put("msg","未获得授权");
        result.put("status",401);
        return result;
    }
}
