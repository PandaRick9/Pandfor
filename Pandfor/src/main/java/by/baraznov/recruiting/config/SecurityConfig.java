package by.baraznov.recruiting.config;

import by.baraznov.recruiting.security.CustomLoginFailureHandler;
import by.baraznov.recruiting.security.CustomLoginSuccessHandler;
import by.baraznov.recruiting.services.impl.PersonDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(requests -> requests

                        .requestMatchers("/auth/login", "/auth/registration", "/job", "/company","/resume", "/aboutus", "/secret-admin-access/**"
                                ,"/search", "/error", "/static/**", "/auth/logo.png", "/css/**", "/js/**", "/images/**").permitAll()

                        .anyRequest().hasAnyRole("JOBSEEKER", "COMPANY","ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .successHandler(customLoginSuccessHandler())
                        .failureHandler(customLoginFailureHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));
        return http.build();
    }
    @Bean
    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler();
    }
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


}
