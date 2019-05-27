//package com.example.demo.config;
//
//import com.example.demo.realm.DBRealm;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.filter.authc.LogoutFilter;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.apache.shiro.web.util.WebUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.Filter;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import java.io.Serializable;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * @Package: com.example.demo.config
// * @ClassName: ShiroConfig
// * @Author: tyq
// * @Description: ${description}
// * @Version: 1.0
// */
//@Configuration
//@Component
//public class ShiroConfig {
//    @Value("${MyRedis.globalSessionTimeout}")
//    private int SESSION_TIME_OUT;
//    @Value("${MyRedis.cookieTimeout}")
//    private int COOCKIE_TIME_OUT;
//
//    @Bean
//    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    /**
//     * ShiroFilterFactoryBean 处理拦截资源文件问题。
//     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
//     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
//     *
//     Filter Chain定义说明
//     1、一个URL可以配置多个Filter，使用逗号分隔
//     2、当设置多个过滤器时，全部验证通过，才视为通过
//     3、部分过滤器可指定参数，如perms，roles
//     *
//     */
//
//    @Bean
//    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
//        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
//        // 必须设置 SecurityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
//        filters.put("custom", new ShiroUserFilter());
//        LogoutFilter logoutFilter = new LogoutFilter();
//        logoutFilter.setRedirectUrl("/logoutSuccess");
//        filters.put("logout", logoutFilter);
////        filters.put("authc", new MyAuthenticationFilter());
//
//        shiroFilterFactoryBean.setFilters(filters);
//
////        拦截器
//        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
//
//        //配置映射关系-----------拦截的是controller中的路径！！！不是静态文件的位置！！！！！
//        filterChainDefinitionMap.put("/tologin", "anon");
//        filterChainDefinitionMap.put("/Captcha.jpg","anon");
////        退出登陆操作！！！doLogout可以不再controller中出现，shiro拦截它后直接执行登陆退出操作（也可以自己实现subject.logout()---适用于退出登陆前还有其他自定义操作）
//        filterChainDefinitionMap.put("/logout", "logout");
//        //未登陆用户访问需登陆页面重定向的地址
//        shiroFilterFactoryBean.setLoginUrl("/unLogin");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");
////        不能只拦截部分资源，而是把所有拦截下来统一管理。
//        filterChainDefinitionMap.put("/**", "custom");
////        filterChainDefinitionMap.put("/**", "user");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public SecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//        //设置realm.
//        securityManager.setRealm(getDBRealm());
//        // 配置 sessionManager
//        securityManager.setSessionManager(sessionManager());
//        return securityManager;
//    }
//
//    @Bean
//    public DBRealm getDBRealm(){
//        DBRealm myShiroRealm = new DBRealm();
////        将md5加密判别扔给了shiro,此处要告诉shiro用那种加密工具
//        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return myShiroRealm;
//    }
//    /**
//     * session 管理对象
//     *
//     * @return DefaultWebSessionManager
//     */
//    private DefaultWebSessionManager sessionManager() {
//        MySessionManager sessionManager = new MySessionManager();
//        // 设置session超时时间，单位为毫秒(@VALUE参数绑定不支持long型，所以要手动转换)
//        long long_SESSION_TIME_OUT=SESSION_TIME_OUT;
//        sessionManager.setGlobalSessionTimeout(long_SESSION_TIME_OUT);
////        默认名为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
//        sessionManager.setSessionIdCookie(new SimpleCookie("sid"));
//        // 必须自定义sessionDAO
//        return sessionManager;
//    }
//
///*
//   传统结构项目中，shiro从cookie中读取sessionId以此来维持会话，
//   在前后端分离的项目中（也可在移动APP项目使用），我们选择在ajax的请求头中传递sessionId，
//   因此需要重写shiro获取sessionId的方式。自定义MySessionManager类继承DefaultWebSessionManager类，重写getSessionId方法
//*/
//
//    public class MySessionManager  extends DefaultWebSessionManager {
//
//        private static final String AUTHORIZATION = "Authorization";
//
//        private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";
//
//        public MySessionManager() {
//            super();
//        }
//
//        @Override
//        protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
//                String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
//            //如果请求头中有 Authorization 则其值为sessionId
//            if (!StringUtils.isEmpty(id)) {
//                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
//                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
//                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
//                return id;
//            } else {
//                //否则按默认规则从cookie取sessionId
//                return super.getSessionId(request, response);
//            }
//
//        }
//    }
//
//    /**
//     * shiro验证密码调用的加密器
//     * （由于密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
//     *  所以还需要修改下doGetAuthenticationInfo中的代码;
//     * ）
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(3);//散列的次数;
//
//        return hashedCredentialsMatcher;
//    }
//
//}
