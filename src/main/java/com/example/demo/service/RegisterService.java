package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Package: com.example.demo.service
 * @ClassName: RegisterService
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@Service
public class RegisterService {
    @Autowired
    private UserMapper userMapper;
//    判断用户是否存在
    public boolean hasUser(String name){

        try {
        User user = userMapper.getbyName(name);
        if (user!=null){
            return true;
        }
        }catch (Exception e){
            return false;
        }return false;
    }
//    注册新用户。若写入失败，则抛出RuntimeException异常。系统捕获到之后会进行回滚！但是不能catch到此异常后自己处理！！！
    @Transactional
    public void register(String name,String password)throws Exception{
//        生成随机字符串作为加密盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
//        MD5加密3次（自定义）得到加密后密码
        String encodedPassword = new SimpleHash("md5",password,salt,3).toString();
        userMapper.save(name,encodedPassword,salt);
    }
}
