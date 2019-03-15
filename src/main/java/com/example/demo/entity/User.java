package com.example.demo.entity;

/**
 * @Package: com.example.demo.entity
 * @ClassName: User
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
public class User {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private Integer id;
    private String name;
    private String password;
    private String role;
}
