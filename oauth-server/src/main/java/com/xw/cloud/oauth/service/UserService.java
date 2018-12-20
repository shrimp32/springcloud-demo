package com.xw.cloud.oauth.service;


import com.xw.cloud.oauth.domain.SysUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * @author : 夏玮
 * Created on 2018/9/10 14:11
 */
@Service
public class UserService {

    private static HashSet<SysUser> users = new HashSet<>();
    public UserService(){
        //数据初始化
        SysUser user1 = new SysUser();
        user1.setUserId(1);
        user1.setName("admin");
        user1.setPwd("admin");
        user1.setEmail("aa@aa.com");
        user1.getAge(33);
        user1.setDept("dept1");
        user1.setRole("role_admin");

        SysUser user2 = new SysUser();
        user2.setUserId(2);
        user2.setName("user");
        user2.setPwd("user");
        user2.setEmail("bb@bb.com");
        user2.getAge(22);
        user2.setDept("dept1");
        user2.setRole("role_user");

        users.add(user1);
        users.add(user2);
    }
    public SysUser findByLoginName(String username){
        for (SysUser user:users){
            if(user.getName().equalsIgnoreCase(username)) return user;
        }
        return null;
    }

}
