//package com.thitiwas.reactive.democonnectapi.security;
//
//
//import com.thitiwas.reactive.democonnectapi.service.ErrorService;
//import com.thitiwas.reactive.democonnectapi.service.TokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.AuthenticationFilter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private JwtAuthenticationProvider jwtAuthenticationProvider;
//
//    @Autowired
//    private TokenService tokenService;
//
//    @Autowired
//    private ErrorService errorService;
//    @Autowired
//    private HandlerExceptionResolver handlerExceptionResolver;
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//
//        http.csrf().disable();
//
//
//        http.antMatcher("/**/s/**").authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new AuthenticationFilter(tokenService, errorService ,handlerExceptionResolver), BasicAuthenticationFilter.class);
//    }
//
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(jwtAuthenticationProvider);
//    }
//}
