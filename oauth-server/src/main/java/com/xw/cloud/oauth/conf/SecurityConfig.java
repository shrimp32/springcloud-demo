package com.xw.cloud.oauth.conf;
import com.xw.cloud.oauth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
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


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**", "/login/**", "/logout").permitAll()
                .anyRequest().authenticated()   // 其他地址的访问均需验证权限
                .and()
                .formLogin()   //指定支持基于表单的身份验证
//                .loginPage("/login")  //不制定则用默认登陆页
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
    /**
     * 各种验证方式
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 内存验证
         */
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("role_admin")
                .and()
                .withUser("user").password(passwordEncoder().encode("user")).roles("role_user");
        /**
         * LDAP验证
         * 基于JDBC验证
         * 添加AuthenticationProvider
         */
//            auth.authenticationProvider(customLdapAuthenticationProvider());
//        auth.authenticationProvider(dbAuthenticationProvider());
//            auth.authenticationProvider(httpAuthenticationProvider());

        /**
         * Service中提供验证逻辑
         */
        auth.userDetailsService(customUserDetailsService);
        auth.parentAuthenticationManager(authenticationManagerBean());


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

//    @Bean
//    public DBAuthenticationProvider dbAuthenticationProvider() {
//        return new DBAuthenticationProvider();
//    }
}

