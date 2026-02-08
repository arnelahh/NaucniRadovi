package main.naucniradovi;

import main.naucniradovi.model.Korisnik;
import main.naucniradovi.repository.KorisnikRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NaucniRadoviApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaucniRadoviApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedAdmin(KorisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (korisnikRepository.findByEmail("admin@admin.ba").isEmpty()) {
                Korisnik admin = new Korisnik();
                admin.setIme("Admin");
                admin.setPrezime("Admin");
                admin.setEmail("admin@admin.ba");
                admin.setLozinka(passwordEncoder.encode("admin"));
                admin.setUloga("ROLE_ADMIN");
                korisnikRepository.save(admin);
            }
        };
    }
}
