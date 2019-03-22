package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
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
            return true;
        }catch (Exception e){
            return false;
        }
    }
//    注册新用户。若写入失败，则抛出RuntimeException异常。系统捕获到之后会进行回滚！但是不能catch到此异常后自己处理！！！
@Transactional
    public void register(String name,String password)throws Exception{
            userMapper.save(name,password);
    }
}
