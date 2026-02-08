package main.naucniradovi.service;

import main.naucniradovi.model.Korisnik;
import org.springframework.stereotype.Service;
import main.naucniradovi.repository.KorisnikRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class KorisnikService {
    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;

    public KorisnikService(KorisnikRepository korisnikRepository, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metoda za registraciju
    public void registrujKorisnika(Korisnik k) {
        System.out.println("--- SERVIS: Počinje registracija za: " + k.getEmail()); // <--- NOVO
        // Provjeri postoji li već email
        if (korisnikRepository.findByEmail(k.getEmail()).isPresent()) {
            throw new RuntimeException("Korisnik sa ovim emailom već postoji!");
        }

        if (k.getUloga() == null || k.getUloga().isEmpty()) {
            k.setUloga("ROLE_USER");
            System.out.println("--- SERVIS: Postavljena default uloga: ROLE_USER");
        }
        // Šifriraj lozinku prije čuvanja
        k.setLozinka(passwordEncoder.encode(k.getLozinka()));
        System.out.println("--- SERVIS: Lozinka šifrirana.");

        try {
            Korisnik spasen = korisnikRepository.save(k);
            System.out.println("--- SERVIS: USPJESNO SPAŠEN! ID: " + spasen.getId()); // <--- KLJUČNO
        } catch (Exception e) {
            System.out.println("--- SERVIS: GREŠKA KOD SPAŠAVANJA U BAZU: " + e.getMessage()); // <--- NOVO
            e.printStackTrace();
        }
    }

    public Optional<Korisnik> nadjiPoEmailu(String email) {
        return korisnikRepository.findByEmail(email);
    }

    public List<Korisnik> sviKorisnici() {
        return korisnikRepository.findAll();
    }

    public void promijeniUlogu(Long id, String uloga) {
        if (uloga == null || uloga.isBlank()) {
            throw new RuntimeException("Uloga ne moze biti prazna.");
        }
        if (!uloga.equals("ROLE_USER") && !uloga.equals("ROLE_ADMIN")) {
            throw new RuntimeException("Nepoznata uloga: " + uloga);
        }
        Korisnik korisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronadjen: " + id));
        korisnik.setUloga(uloga);
        korisnikRepository.save(korisnik);
    }
}
