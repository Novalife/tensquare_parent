package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 * 我们在添加了spring security依赖后，所有的地址都被spring security所控制了，我们目
 * 前只是需要用到BCrypt密码加密的部分，所以我们要添加一个配置类，配置为所有地址
 * 都可以匿名访问。
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll().anyRequest()
                .authenticated()
                .and().csrf().disable();
    }
}