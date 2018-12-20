package com.xw.cloud.oauth.service;

import com.xw.cloud.oauth.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 授权的时候是对角色授权，而认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取系统用户信息
        SysUser sysUser = userService.findByLoginName(username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 以权限名封装为Spring的security Object
        authorities.add(new SimpleGrantedAuthority(sysUser.getRole()));

        return new User(sysUser.getName(), sysUser.getPwd(), authorities);
    }
}
