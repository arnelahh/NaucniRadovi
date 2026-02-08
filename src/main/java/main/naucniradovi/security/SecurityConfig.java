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
                        .requestMatchers("/", "/registracija", "/login", "/css/**", "/js/**").permitAll() // Ovo je javno
                        .requestMatchers("/admin/**", "/admin_korisnici").hasRole("ADMIN")
                        .requestMatchers("/radovi/novi", "/radovi/sacuvaj", "/radovi/obrisi/**", "/radovi/uredi/**", "/radovi/azuriraj/**", "/profil").authenticated() // Ovo zahtijeva login
                        .requestMatchers("/radovi/*/komentar", "/komentari/**").authenticated()
                        .anyRequest().permitAll() // Sve ostalo je dozvoljeno (za sad, radi lakšeg testiranja)
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Naša custom login stranica (napravit ćemo je u HTML-u)
                        .defaultSuccessUrl("/", true) // Gdje ideš kad se uspješno loguješ
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
