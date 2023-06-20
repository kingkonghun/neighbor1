package com.anabada.neighbor.config;

import com.anabada.neighbor.config.handler.CustomAccessDeniedHandler;
import com.anabada.neighbor.config.handler.CustomAuthFailureHandler;
import com.anabada.neighbor.config.handler.CustomAuthSuccessHandler;
import com.anabada.neighbor.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin()
                .usernameParameter("memberEmail")
                .passwordParameter("memberPWD")
                .loginPage("/member/loginForm")
                .loginProcessingUrl("/login")
                .successHandler(customAuthSuccessHandler)
                .failureHandler(customAuthFailureHandler)
                .and()
                .oauth2Login()
                .loginPage("/member/loginForm")
                .successHandler(customAuthSuccessHandler)
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .build();
    }
}
