package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    public boolean hasUser(String name){

        try {
        User user = userMapper.getbyName(name);
        System.out.println(user.getPassword());
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
//    事物回滚，如果添加失败则自动回滚。不会写入垃圾数据
    @Transactional
    public boolean register(String name,String password){
        try {
            userMapper.save(name,password);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
