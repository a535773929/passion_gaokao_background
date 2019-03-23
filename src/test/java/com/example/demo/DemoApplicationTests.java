package com.example.demo;

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
    public void contextLoads() {

        usermapper.save("zhang3","123456");
//        User a = usermapper.getbyName("root");
//        System.out.println(a.getPassword());
    }
//    @Test
//    public void webcontroll() {
//        RegisterService rs = new RegisterService();
//
//        String name = "li4";
//        String password ="123456";
//        if (rs.hasUser(name)){
//            System.out.println("已存在");
//        }else if(!rs.register(name,password)){
//            System.out.println("注册失败");
//        }else {
//            System.out.println("成功");
//        }
//    }
    @Test
    public void getByName() {
        String name = "ma6";
        String password ="123456";
        System.out.println(rs.hasUser(name));
    }
    @Test
    public void getByName2() {
        String name = usermapper.getbyName("li4").getPassword();
        System.out.println(name);
    }
    @Test
    public void getPerms() {
        System.out.println(usermapper.getPerms("root"));
    }
}
