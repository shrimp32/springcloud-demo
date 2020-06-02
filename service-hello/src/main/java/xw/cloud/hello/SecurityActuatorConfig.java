package xw.cloud.hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author : 夏玮
 * Created on 2019-07-19 17:03
 */
@Configuration
public class SecurityActuatorConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
//                //拦截所有endpoint，拥有ACTUATOR_ADMIN角色可访问，否则需登录
//                .anyRequest().hasRole("ACTUATOR_ADMIN")
//                //根路径允许访问
//                .antMatchers("/").permitAll()
//                //所有请求路径可以访问
//                .antMatchers("/**").permitAll()
//                .and()
//                .httpBasic().and().csrf().disable();
//
//    }

    /**
     * 允许所有的访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll()
                .and().csrf().disable();
    }
}
