package com.example.todo.security;

import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) return user.get();
            throw new UsernameNotFoundException(email + " not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                   .requestMatchers("/task/**", "/list/**", "/manage/**", "/settings/**").authenticated()
                   .requestMatchers("/", "/**").permitAll()
                   .and()
                       .formLogin()
                       .usernameParameter("email")
                       .loginPage("/login")
                       .defaultSuccessUrl("/manage/today")
                   .and()
                       .logout()
                       .logoutUrl("/logout")
                       .logoutSuccessUrl("/")
                       .invalidateHttpSession(true)
                   .and().build();
    }

}
