//package com.example.demo.config;
//
//import cn.hutool.core.codec.Base64;
//import com.example.demo.cache.MySessionDAO;
//import com.example.demo.cache.ShiroRedisCacheManager;
//import com.example.demo.realm.DBRealm;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
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
//    @Autowired
//    RedisTemplate redisTemplate;
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
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面，即登陆主页面。此处的链接一样要经过controller！！！！所以不能直接写页面地址
//       //如果点击了需要登录才能访问的页面，就会自动跳到这里来
//        shiroFilterFactoryBean.setLoginUrl("/login");
////      登录成功后要跳转的链接(此配置仅仅作为附加配置，session中无用户访问记录时才会往此地址跳。且需要自己配置，不采用此法！）
//        shiroFilterFactoryBean.setSuccessUrl("/operate");
//        //未授权界面;
////        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");
////        拦截器
//        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
//
//        //配置映射关系-----------拦截的是controller中的路径！！！不是静态文件的位置！！！！！
//        filterChainDefinitionMap.put("/tologin", "anon");
//        filterChainDefinitionMap.put("/Captcha.jpg","anon");
////        filterChainDefinitionMap.put("/adduser", "perms[add]");
////        filterChainDefinitionMap.put("/updateuser", "perms[update]");
////        退出登陆操作！！！doLogout可以不再controller中出现，shiro拦截它后直接执行登陆退出操作（也可以自己实现subject.logout()---适用于退出登陆前还有其他自定义操作）
//        filterChainDefinitionMap.put("/doLogout", "logout");
////        不能只拦截部分资源，而是把所有拦截下来统一管理。
//        filterChainDefinitionMap.put("/**", "user");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public SecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//        // 配置 rememberMeCookie 查看源码可以知道，这里的rememberMeManager就仅仅是一个赋值，所以先执行
//        securityManager.setRememberMeManager(rememberMeManager());
//        // 配置 缓存管理类 cacheManager，这个cacheManager必须要在前面执行，因为setRealm 和 setSessionManage都有方法初始化了cachemanager,看下源码就知道了
//        securityManager.setCacheManager(cacheManager(redisTemplate));
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
//     * 生成一个ShiroRedisCacheManager 这没啥好说的
//     **/
//    private ShiroRedisCacheManager cacheManager(RedisTemplate redisTemplate){
//        return new ShiroRedisCacheManager(redisTemplate);
//    }
//    /**
//     * session 管理对象
//     *
//     * @return DefaultWebSessionManager
//     */
//    private DefaultWebSessionManager sessionManager() {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        // 设置session超时时间，单位为毫秒(@VALUE参数绑定不支持long型，所以要手动转换)
//        long long_SESSION_TIME_OUT=SESSION_TIME_OUT;
//        sessionManager.setGlobalSessionTimeout(long_SESSION_TIME_OUT);
////        默认名为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
//        sessionManager.setSessionIdCookie(new SimpleCookie("sid"));
//        // 必须自定义sessionDAO
//        sessionManager.setSessionDAO(new MySessionDAO());
//        return sessionManager;
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
//////   shiro和thymeleaf整合需要返回此对象
////
////    @Bean
////    public ShiroDialect getShiroDialect(){
////        return new ShiroDialect();
////    }
//    /**
//     * rememberMe cookie 效果是重开浏览器后无需重新登录
//     *
//     * @return SimpleCookie
//     */
//    private SimpleCookie rememberMeCookie() {
//        // 这里的Cookie的默认名称是 CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME
//        SimpleCookie cookie = new SimpleCookie("rememberMe");
//        // 是否只在https情况下传输
//        cookie.setSecure(false);
//        // 设置 cookie 的过期时间，单位为秒，这里为一天
//        cookie.setMaxAge(COOCKIE_TIME_OUT);
//        return cookie;
//    }
//
//    /**
//     * cookie管理对象
//     *
//     * @return CookieRememberMeManager
//     */
//    private CookieRememberMeManager rememberMeManager() {
//        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
//        cookieRememberMeManager.setCookie(rememberMeCookie());
//        // rememberMe cookie 加密的密钥(必须是16位秘钥！！！！！！！！！！！！！！！！！）
//        cookieRememberMeManager.setCipherKey(Base64.decode("qdaZWvohmPdUsAWT=12kml5"));
//        return cookieRememberMeManager;
//    }
//}
