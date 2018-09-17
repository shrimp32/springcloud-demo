package cn.com.taiji.support;

import cn.com.taiji.domain.Menu;
import cn.com.taiji.dto.RoleDTO;
import cn.com.taiji.dto.UserDTO;
import cn.com.taiji.repository.MenuRepository;
import cn.com.taiji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuRepository menuRepository;

    /**
     * 授权的时候是对角色授权，而认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO sysUser = userService.findByLoginName(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException(username);
        }

        if (!"1".equals(sysUser.getState())) {
            throw new UsernameNotFoundException("该用户处于锁定状态");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 以权限名封装为Spring的security Object
        Set<RoleDTO> roleDTOs = sysUser.getRoles();
        for (RoleDTO roleDTO : roleDTOs) {
            List<Menu> menus = this.menuRepository.getMenuListByRole(roleDTO.getId());
            if (ListUtils.isEmpty(menus)) {
                continue;
            }
            for (Menu menu : menus) {
                authorities.add(new SimpleGrantedAuthority(menu.getId()));
            }
        }

        return new User(sysUser.getUserName(), sysUser.getPassword(), authorities);
    }
}
