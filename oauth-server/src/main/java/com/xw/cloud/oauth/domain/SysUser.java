package com.xw.cloud.oauth.domain;

/**
 * @author : 夏玮
 * Created on 2018/9/10 14:07
 */

public class SysUser {
    private String name;
    private String pwd;
    private Integer age;
    private Integer userId;
    private String dept;
    private String email;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDept() {

        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getAge(Integer integer) {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
