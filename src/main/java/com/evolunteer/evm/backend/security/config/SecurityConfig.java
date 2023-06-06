package com.evolunteer.evm.backend.security.config;

import com.evolunteer.evm.backend.security.cache.CustomRequestCache;
import com.evolunteer.evm.backend.security.handler.LoginPageAuthenticationSuccessHandler;
import com.evolunteer.evm.backend.security.handler.Oauth2FailureHandler;
import com.evolunteer.evm.backend.security.handler.Oauth2SuccessHandler;
import com.evolunteer.evm.backend.security.utils.SecurityUtils;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private UserService userService;
    private AccountService accountService;

    @Autowired
    @Lazy
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    @Lazy
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Require login to access internal pages and configure login form.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(new CustomRequestCache())
                .and()
                .authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers(SecurityUtils.Route.allowedRoutes())
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(SecurityUtils.Route.INTRODUCTION_ROUTE).permitAll()
                .successHandler(this.loginPageSuccessHandler())
                .loginProcessingUrl(SecurityUtils.Route.ACCOUNT_LOGIN_ROUTE)
                .failureUrl(SecurityUtils.Route.ACCOUNT_LOGIN_FAILURE_ROUTE)
                .and()
                .oauth2Login()
                .loginPage(SecurityUtils.Route.OAUTH_GOOGLE_LOGIN_ROUTE)
                .successHandler(this.oauthSuccessHandler())
                .failureHandler(this.oauthFailureHandler())
                .and()
                .logout()
                .logoutUrl(SecurityUtils.Route.ACCOUNT_LOGOUT_ROUTE)
                .logoutSuccessUrl(SecurityUtils.Route.ACCOUNT_LOGIN_ROUTE)
                .invalidateHttpSession(true)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.daoAuthenticationProvider());
    }

    /**
     * Allows access to static resources, bypassing Spring Security.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // Client-side JS
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/offline-stub.html",

                // icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/META-INF/**",

                // (development mode) H2 debugging console
                "/h2-console/**");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(accountService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler oauthSuccessHandler() {
        return new Oauth2SuccessHandler(accountService, userService);
    }

    @Bean
    public AuthenticationSuccessHandler loginPageSuccessHandler() {
        return new LoginPageAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler oauthFailureHandler() {
        return new Oauth2FailureHandler();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
