package com.example.demo.entity;

import java.io.Serializable;

/**
 * @Package: com.example.demo.entity
 * @ClassName: User
 * @Author: tyq
 * @Description: ${description}
 * @Version: 1.0
 */
public class  User implements Serializable {

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


    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    private String name;
    private String password;
    private String salt;
}
