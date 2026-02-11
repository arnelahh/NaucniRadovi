package main.naucniradovi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registracija", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**", "/admin_korisnici").hasRole("ADMIN")
                        .requestMatchers("/radovi/novi", "/radovi/sacuvaj", "/radovi/obrisi/**", "/radovi/uredi/**", "/radovi/azuriraj/**", "/profil").authenticated()
                        .requestMatchers("/radovi/*/komentar", "/komentari/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
