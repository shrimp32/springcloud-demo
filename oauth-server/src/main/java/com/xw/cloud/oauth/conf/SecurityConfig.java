package cn.com.taiji.config;

import cn.com.taiji.support.CustomLdapAuthenticationProvider;
import cn.com.taiji.support.CustomUserDetailsService;
import cn.com.taiji.support.DBAuthenticationProvider;
import cn.com.taiji.support.HttpAuthenticationProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private SysConfig sysConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**", "/login/**", "/logout").permitAll()
                .anyRequest().authenticated()   // 其他地址的访问均需验证权限
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/");

//        http.authorizeRequests().accessDecisionManager(null);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/theme/**", "/images/**", "/fonts/**"
                , "/css/**", "/js/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (ArrayUtils.contains(sysConfig.getProviderTypes(), SysConfig.ProviderType.LDAP)) {
            auth.authenticationProvider(customLdapAuthenticationProvider());
        }

        if (ArrayUtils.contains(sysConfig.getProviderTypes(), SysConfig.ProviderType.DB)) {
            auth.authenticationProvider(dbAuthenticationProvider());
        }

        if (ArrayUtils.contains(sysConfig.getProviderTypes(), SysConfig.ProviderType.HTTP)) {
            auth.authenticationProvider(httpAuthenticationProvider());
        }

        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpAuthenticationProvider httpAuthenticationProvider() {
        return new HttpAuthenticationProvider();
    }

    @Bean
    public CustomLdapAuthenticationProvider customLdapAuthenticationProvider() {
        return new CustomLdapAuthenticationProvider();
    }

    @Bean
    public DBAuthenticationProvider dbAuthenticationProvider() {
        return new DBAuthenticationProvider();
    }
}

