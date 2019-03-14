package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Package: com.example.demo.service
 * @ClassName: RegisterService
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
public class RegisterService {
    @Autowired UserMapper usermapper;
    public boolean hasUser(String name){

        try {
            usermapper.getbyName(name).getName();
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean register(String name,String password){
        try {
            usermapper.save(name,password);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
