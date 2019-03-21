package com.example.demo.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.example.demo.cache.MySessionDAO;
import com.example.demo.cache.ShiroRedisCacheManager;
import com.example.demo.realm.DBRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.*;

/**
 * @Package: com.example.demo.config
 * @ClassName: ShiroConfig
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@Configuration
@Component
//@ConfigurationProperties(prefix = "hayek.shiro")
public class ShiroConfig {
    @Autowired
    RedisTemplate redisTemplate;
//    @Value("${hayek.shiro.sessionTimeout}")
//    private long sessionTimeout;
//    @Value("${hayek.shiro.sessionIdName}")
//    private String SessionIdName;
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
        filterChainDefinitionMap.put("/adduser", "perms[add]");
        filterChainDefinitionMap.put("/updateuser", "perms[update]");
//        退出登陆操作！！！doLogout可以不再controller中出现，shiro拦截它后直接执行登陆退出操作（也可以自己实现subject.logout()---适用于退出登陆前还有其他自定义操作）
        filterChainDefinitionMap.put("/doLogout", "logout");
//        不能只拦截部分资源，而是把所有拦截下来统一管理。
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
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
        return new ShiroRedisCacheManager(template);
    }
    /**
     * session 管理对象
     *
     * @return DefaultWebSessionManager
     */
    private DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置session超时时间，单位为毫秒
        sessionManager.setGlobalSessionTimeout(1800000l);
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
     * 解决： 无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized") 无效
     * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
     * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
     * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
     * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver=new SimpleMappingExceptionResolver();
        Properties properties=new Properties();
        //这里的 /unauthorized 是页面，不是访问的路径
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException","/unAuth");
        properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","/unAuth");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }
}
