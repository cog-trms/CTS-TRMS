package com.cognizant.trms.security;

import com.cognizant.trms.security.api.ApiJWTAuthenticationFilter;
import com.cognizant.trms.security.api.ApiJWTAuthorizationFilter;
import com.cognizant.trms.security.form.CustomAuthenticationSuccessHandler;
import com.cognizant.trms.security.form.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Aravindan Dandapani
 */
@EnableWebSecurity
public class MultiHttpSecurityConfig {

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        // @formatter:off
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors()
                    .and()
                    .csrf()
                        .disable()
                    .antMatcher("/api/**")
                    .authorizeRequests()
                        .antMatchers("/api/v1/user/signup").permitAll()
                      //  .antMatchers("/api/v1/user/listUsers").permitAll()
                    //.antMatchers("/api/v1/account/all").permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                    .logout()
                    .permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                    .and()
                    .exceptionHandling()
                        .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .and()
                    .addFilter(new ApiJWTAuthenticationFilter(authenticationManager()))
                    .addFilter(new ApiJWTAuthorizationFilter(authenticationManager()))
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
        // @formatter:on
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(
                    "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                    "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
                    "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png",
                    "/v2/api-docs", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
                    "/webjars/**", "/swagger-resources/**", "/swagge‌​r-ui.html", "/actuator",
                    "/actuator/**");
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

    }

//    @Order(2)
//    @Configuration
//    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//        @Autowired
//        private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//        @Autowired
//        private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
//
//        @Autowired
//        private CustomUserDetailsService userDetailsService;
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth
//                    .userDetailsService(userDetailsService)
//                    .passwordEncoder(bCryptPasswordEncoder);
//        }
//
//        // @formatter:off
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .cors()
//                    .and()
//                    .csrf()
//                        .disable()
//                    .authorizeRequests()
//                        .antMatchers("/").permitAll()
//                        .antMatchers("/login").permitAll()
//                        .antMatchers("/signup").permitAll()
//                        .antMatchers("/dashboard/**").hasAuthority("ADMIN")
//                    .anyRequest()
//                        .authenticated()
//                    .and()
//                    .formLogin()
//                        .loginPage("/login")
//                        .permitAll()
//                        .failureUrl("/login?error=true")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .successHandler(customAuthenticationSuccessHandler)
//                    .and()
//                    .logout()
//                        .permitAll()
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true)
//                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
//                        .deleteCookies("JSESSIONID")
//                        .logoutSuccessUrl("/")
//                    .and()
//                        .exceptionHandling();
//        }
//
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers(
//                    "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
//                    "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
//                    "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png",
//                    "/v2/api-docs", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
//                    "/webjars/**", "/swagger-resources/**", "/swagge‌​r-ui.html", "/actuator",
//                    "/actuator/**");
//        }
//        // @formatter:on
//
//        @Bean
//        CorsConfigurationSource corsConfigurationSource() {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000"));
//            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", configuration);
//            return source;
//        }
//    }
}
