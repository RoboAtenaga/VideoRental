package com.rensource.videorental.config;

import com.rensource.videorental.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable().and().authorizeRequests()
                .antMatchers("/api/login", "/api/index").permitAll()
//                .antMatchers("/api/index").permitAll()
                .antMatchers( "/api/**").authenticated()
                .anyRequest().authenticated()
//                .and().formLogin()
//                .loginPage("/login").defaultSuccessUrl("/index")
                .and().httpBasic();
//                .and().formLogin()

//                .antMatchers( "/video/**","/rental/**").authenticated();
//                .antMatchers( "/video/**","/rental/**").access("hasAuthority('USER')")
//                .anyRequest().authenticated();
//                .and().formLogin()
//                .loginPage("/login").defaultSuccessUrl("/index")
//                .failureUrl("/login?error").permitAll()
//                .and()
//                .logout().invalidateHttpSession(true)
//                .clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login?logout=true").permitAll();
    }


}
