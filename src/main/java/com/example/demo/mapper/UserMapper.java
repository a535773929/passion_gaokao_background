package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Package: com.example.demo.mapper
 * @ClassName: UserMapper
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
@Mapper
@Component
public interface UserMapper {
//    根据姓名查找用户
    @Select("select * from customer_service where customer_service_id= #{name} ")
    @Results({
            @Result(property="name",column="customer_service_id"),
            @Result(property="password",column="password"),
            @Result(property="salt",column="salt")
    })
    User getbyName(String name);
//    添加新用户
    @Insert({"insert into customer_service(customer_service_id, password,salt) values(#{name}, #{password},#{salt})"})
    int save(@Param("name") String name, @Param("password")String password,@Param("salt")String salt);
////    查询用户权限
//    @Select("select tp.permission from test_user tu left join test_permission tp on tu.role = tp.role where name = #{name}" )
//    List<String> getPerms(@Param("name") String name);
}
