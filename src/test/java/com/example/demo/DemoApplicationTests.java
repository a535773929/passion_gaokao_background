package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.RegisterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(basePackages = "com.example.demo.mapper")
public class DemoApplicationTests {
    @Autowired UserMapper usermapper;
    @Autowired RegisterService rs;

    @Test
    public void webcontroll() {
        RegisterService rs = new RegisterService();

        String name = "li4";
        String password ="123456";
        try {
            if (rs.hasUser(name)) {
                System.out.println("已存在");
            } else {rs.register(name, password);}
        } catch (Exception e) {
            System.out.println("注册失败");;
        }
    }

    @Test
    public void getByName() {
        String name = usermapper.getbyName("li4").getPassword();
        System.out.println(name);
    }
}

