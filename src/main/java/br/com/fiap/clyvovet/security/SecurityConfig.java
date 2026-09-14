package br.com.fiap.clyvovet.security;

import br.com.fiap.clyvovet.model.Perfil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(autorizacao -> autorizacao
                        .requestMatchers("/api/tutor/**").hasRole(Perfil.TUTOR.name())
                        .requestMatchers("/api/veterinario/**").hasRole(Perfil.VETERINARIO.name())
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(autorizacao -> autorizacao
                        .requestMatchers("/login", "/css/**", "/error").permitAll()
                        .requestMatchers("/tutor/**").hasRole(Perfil.TUTOR.name())
                        .requestMatchers("/veterinario/**").hasRole(Perfil.VETERINARIO.name())
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
