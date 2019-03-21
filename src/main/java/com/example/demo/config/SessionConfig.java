//package com.example.demo.config;
//
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.SessionCookieConfig;
//
///**
// * @Package: com.example.demo.config
// * @ClassName: SessionConfig
// * @Author: tyq
// * @Description: ${description}
// * @Version: 1.0
// */
//@Configuration
//public class SessionConfig{
//    /**
//     * 配置保存sessionId的cookie
//     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
//     * 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
//     * @return
//     */
//    @Bean("sessionIdCookie")
//    public SimpleCookie sessionIdCookie(){
//        //这个参数是cookie的名称
//        SimpleCookie simpleCookie = new SimpleCookie("sid");
//        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
//
//        //setcookie()的第七个参数
//        //设为true后，只能通过http访问，javascript无法访问
//        //防止xss读取cookie
//        simpleCookie.setHttpOnly(true);
//        simpleCookie.setPath("/");
//        //maxAge=-1表示浏览器关闭时失效此Cookie
//        simpleCookie.setMaxAge(-1);
//        return simpleCookie;
//    }
//}
