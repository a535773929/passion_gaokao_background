package com.example.demo.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.hutool.core.codec.Base64;
import com.example.demo.cache.MySessionDAO;
import com.example.demo.cache.ShiroRedisCacheManager;
import com.example.demo.realm.DBRealm;
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
//import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
//import java.util.*;

/**
 * @Package: com.example.demo.config
 * @ClassName: ShiroConfig
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@Configuration
@Component
public class ShiroConfig {
    @Value("${MyRedis.globalSessionTimeout}")
    private int session_time_out;
    @Value("${MyRedis.cookieTimeout}")
    private int COOCKIE_TIME_OUT;
    @Autowired
    RedisTemplate redisTemplate;
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     Filter Chain定义说明
     1、一个URL可以配置多个Filter，使用逗号分隔
     2、当设置多个过滤器时，全部验证通过，才视为通过
     3、部分过滤器可指定参数，如perms，roles
     *
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面，即登陆主页面。此处的链接一样要经过controller！！！！所以不能直接写页面地址
       //如果点击了需要登录才能访问的页面，就会自动跳到这里来
        shiroFilterFactoryBean.setLoginUrl("/login");
//         登录成功后要跳转的链接(此配置仅仅作为附加配置，session中无用户访问记录时才会往此地址跳。且需要自己配置，不采用此法！）
        shiroFilterFactoryBean.setSuccessUrl("/operate");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");
//        拦截器uj
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

        //配置映射关系-----------拦截的是controller中的路径！！！不是静态文件的位置！！！！！
        filterChainDefinitionMap.put("/tologin", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/toregister", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/unAuth", "anon");
        filterChainDefinitionMap.put("/test", "anon");
        filterChainDefinitionMap.put("/Captcha.jpg","anon");
        filterChainDefinitionMap.put("/adduser", "perms[add]");
        filterChainDefinitionMap.put("/updateuser", "perms[update]");
//        退出登陆操作！！！doLogout可以不再controller中出现，shiro拦截它后直接执行登陆退出操作（也可以自己实现subject.logout()---适用于退出登陆前还有其他自定义操作）
        filterChainDefinitionMap.put("/doLogout", "logout");
//        不能只拦截部分资源，而是把所有拦截下来统一管理。
        filterChainDefinitionMap.put("/**", "user");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        // 配置 rememberMeCookie 查看源码可以知道，这里的rememberMeManager就仅仅是一个赋值，所以先执行
        securityManager.setRememberMeManager(rememberMeManager());
        // 配置 缓存管理类 cacheManager，这个cacheManager必须要在前面执行，因为setRealm 和 setSessionManage都有方法初始化了cachemanager,看下源码就知道了
        securityManager.setCacheManager(cacheManager(redisTemplate));
        //设置realm.
        securityManager.setRealm(getDBRealm());
        // 配置 sessionManager
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    /**
     * 生成一个ShiroRedisCacheManager 这没啥好说的
     **/
    private ShiroRedisCacheManager cacheManager(RedisTemplate template){
        return new ShiroRedisCacheManager(redisTemplate);
    }
    /**
     * session 管理对象
     *
     * @return DefaultWebSessionManager
     */
    private DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置session超时时间，单位为毫秒(@VALUE参数绑定不支持long型，所以要手动转换)
        long SESSION_TIME_OUT=session_time_out;
        sessionManager.setGlobalSessionTimeout(123456L);
        sessionManager.setSessionIdCookie(new SimpleCookie("sid"));
        // 网上各种说要自定义sessionDAO 其实完全不必要，shiro自己就自定义了一个，可以直接使用，还有其他的DAO，自行查看源码即可
//        sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        sessionManager.setSessionDAO(new MySessionDAO());
        return sessionManager;
    }
    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */



    @Bean
    public DBRealm getDBRealm(){
        DBRealm myShiroRealm = new DBRealm();
        return myShiroRealm;
    }
//   shiro和thymeleaf整合需要返回此对象
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
    /**
     * rememberMe cookie 效果是重开浏览器后无需重新登录
     *
     * @return SimpleCookie
     */
    private SimpleCookie rememberMeCookie() {
        // 这里的Cookie的默认名称是 CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME
        SimpleCookie cookie = new SimpleCookie(CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME);
        // 是否只在https情况下传输
        cookie.setSecure(false);
        // 设置 cookie 的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(COOCKIE_TIME_OUT);
        return cookie;
    }

    /**
     * cookie管理对象
     *
     * @return CookieRememberMeManager
     */
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie 加密的密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("qdaZWvohmPdUsAWT=12kml5"));
        return cookieRememberMeManager;
    }
}
