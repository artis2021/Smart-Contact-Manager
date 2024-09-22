package com.example.SmartContactManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MyConfig {

    // UserDetailsService bean
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication provider bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    // AuthenticationManager bean to support authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // SecurityFilterChain to configure HTTP security
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers("/**").permitAll()
//                )
//                .formLogin().loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and()
//                .csrf().disable();
//
//        return http.build();
//    }

    // SecurityFilterChain to configure HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Only admins can access /admin
                        .requestMatchers("/user/**").hasRole("USER")   // Only authenticated users can access /user
                        .requestMatchers("/signin", "/signup", "/css/**", "/js/**").permitAll() // Public pages like login, signup
                        .anyRequest().authenticated() // All other pages require authentication
                )
                .formLogin(form -> form
                        .loginPage("/signin")                   // Custom login page
                        .loginProcessingUrl("/dologin")          // Login form submits to this URL
                        .defaultSuccessUrl("/user/index")        // Redirect after successful login
                        .failureUrl("/signin?error=true")        // Redirect on login failure
                        .permitAll()                             // Allow everyone to access the login page
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                    // Custom logout URL
                        .logoutSuccessUrl("/signin?logout=true") // Redirect after successful logout
                        .permitAll()                             // Allow everyone to access logout
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/403")                // Custom access denied page for 403 errors
                )
                .csrf().disable(); // Consider re-enabling CSRF for security in production environments

        return http.build();
    }

}
