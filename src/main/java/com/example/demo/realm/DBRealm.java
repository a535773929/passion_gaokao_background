package com.example.demo.realm;

import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.example.demo.realm
 * @ClassName: DBRealm
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
public class DBRealm extends AuthorizingRealm {
//    usermapper注入错误是因为没有标注为@Component
    @Autowired
    private UserMapper userMapper;
//    授权逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //    认证逻辑
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        拿到当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        String currentUsername = subject.getPrincipal().toString();
//        读取当前用户权限并添加到Info
        List<String>perms = userMapper.getPerms(currentUsername);
        info.addStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
 //        根本就不用拿到用户输入的密码，密码判定不是在这一层
//        UsernamePasswordToken t = (UsernamePasswordToken) authenticationToken;
        String userName = authenticationToken.getPrincipal().toString();
        User theUser = userMapper.getbyName(userName);
        String name = theUser.getName();
        String password = theUser.getPassword();
        if (!userName.equals(name)){
//			最后shiro会抛出UnknowAccountException
            return null;
        }
        SimpleAuthenticationInfo a = new SimpleAuthenticationInfo(name,password,getName());
        return a;
    }
}
