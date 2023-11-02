package com.hgleeee.blog.config;

import com.hgleeee.blog.oauth2.CustomOAuth2FailureHandler;
import com.hgleeee.blog.oauth2.CustomOAuth2SuccessHandler;
import com.hgleeee.blog.oauth2.CustomOAuth2UserService;
import com.hgleeee.blog.service.CustomUserDetailsService;
import com.hgleeee.blog.token.*;
import com.hgleeee.blog.token.resolver.DelegatingTokenResolver;
import com.hgleeee.blog.token.resolver.OAuth2TokenResolver;
import com.hgleeee.blog.token.resolver.SimpleTokenResolver;
import com.hgleeee.blog.token.resolver.TokenResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final JwtProperties jwtProperties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher("/css/**"))
                .requestMatchers(new AntPathRequestMatcher("/js/**"))
                .requestMatchers(new AntPathRequestMatcher("/img/**"))
                .requestMatchers(new AntPathRequestMatcher("/lib/**"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer
                            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(mvcMatcherBuilder.pattern("/api/auth/**")).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                            .userInfoEndpoint(
                                    userInfoEndpointConfig -> userInfoEndpointConfig
                                            .userService(customOAuth2UserService))
                            .successHandler(customOAuth2SuccessHandler)
                            .failureHandler(customOAuth2FailureHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        return http.build();
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenResolver());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder(), customUserDetailsService);
    }

    @Bean
    public TokenResolver tokenResolver() {
        return new DelegatingTokenResolver(List.of(
                new SimpleTokenResolver(jwtProperties),
                new OAuth2TokenResolver(jwtProperties)
        ));
    }

}
