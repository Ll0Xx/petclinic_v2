package com.antont.petclinic.v2.security;

import com.antont.petclinic.v2.auth.UserDetailServiceImpl;
import com.antont.petclinic.v2.auth.dto.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailService;

    public SecurityConfig(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/doctor", "/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                .antMatchers("/webjars/**", "/process_register", "/assets").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(getHandler())
                .loginProcessingUrl("/perform_login")
                .successHandler(myAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/").permitAll()
                .and()
                .headers()
                .contentSecurityPolicy("script-src 'self'");
    }

    private AuthenticationFailureHandler getHandler(){
       return (request, response, exception) -> {
           String email = request.getParameter("email");
           String error = exception.getMessage();
           System.out.println("A failed login attempt with email: "
                   + email + ". Reason: " + error);

           String redirectUrl = request.getContextPath() + "/login?error";
           response.sendRedirect(redirectUrl);
       };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler();
    }
}
