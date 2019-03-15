package com.example.demo.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.example.demo.realm.DBRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Package: com.example.demo.config
 * @ClassName: ShiroConfig
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@Configuration
public class ShiroConfig {
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
//        shiroFilterFactoryBean.setSuccessUrl("/test");
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
        //设置realm.
        securityManager.setRealm(getDBRealm());
        return securityManager;
    }
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
}
