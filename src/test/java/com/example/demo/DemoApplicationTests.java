package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AppointmentListService;
import com.example.demo.service.RegisterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(basePackages = "com.example.demo.mapper")
public class DemoApplicationTests {
    @Autowired RegisterService rs;
    @Autowired
    AppointmentListService as;

    @Test
    public void webcontroll() {


        try {
            String name = "root";
            String password ="123456";
            rs.register(name,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            if (rs.hasUser(name)) {
//                System.out.println("已存在");
//            } else {rs.register(name, password);}
//        } catch (Exception e) {
//            System.out.println("注册失败");
//            e.printStackTrace();
//        }
    }

    @Test
    public void getByName() {
        User user = rs.getUser("root");
        System.out.println(user.toString());
    }
    @Test
    public void datTransfer()throws Exception{

        Date now = new Date();
        Date today = new Date(now.getYear(),now.getMonth(),now.getDay());
        System.out.println(today);
    }
    @Test
    public void getAll() {

//        System.out.println(as.findAll());
//        System.out.println("1" + new Integer(2) + 3.0);
        StringTokenizer st = new StringTokenizer("Welcome to China", " ");
        while (st.hasMoreTokens())  {
            System.out.println(123);
            System.out.println(st.nextToken());

        }
    }
    @Test
    public void getStudentInfo() {
        System.out.println(as.findInfo(1).toString());

    }

}

