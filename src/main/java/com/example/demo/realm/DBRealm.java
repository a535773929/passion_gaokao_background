package com.example.demo.realm;

import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.RegisterService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
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
    private RegisterService registerService;
//    授权逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //    认证逻辑
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        用户输入的密码
        UsernamePasswordToken t = (UsernamePasswordToken) authenticationToken;
        String password =new String(t.getPassword());
        String userName = authenticationToken.getPrincipal().toString();
        User theUser = registerService.getUser(userName);
//        从数据库拿出的密码（已加密）
        String passwordInDB = theUser.getPassword();
        String salt = theUser.getSalt();

////        自定义加密比较方法
////        把用户输入的密码用数据库取出的盐加密，得到passwordEncoded
//        String passwordEncoded = new SimpleHash("md5",password,salt,3).toString();
////        如果用户为空或者密码不匹配就抛错，这个错误会在上层去捕获
//        if(null==theUser || !passwordEncoded.equals(passwordInDB)){
//            throw new AuthenticationException();
//        }
////        SimpleAuthenticationInfo AuthInfo = new SimpleAuthenticationInfo(userName,password,getName());

//        把加密比较的操作扔给shiro去判断（上面的方法是自己来判断，不匹配就往上层抛错）
        SimpleAuthenticationInfo AuthInfo = new SimpleAuthenticationInfo(userName,passwordInDB, ByteSource.Util.bytes(salt),getName());
        return AuthInfo;
    }
}
