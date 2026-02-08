package main.naucniradovi.service;

import main.naucniradovi.model.CustomUserDetails;
import main.naucniradovi.model.Korisnik;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import main.naucniradovi.repository.KorisnikRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final KorisnikRepository korisnikRepository;

    public CustomUserDetailsService(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Tražimo korisnika i ODMAH bacamo grešku ako ne postoji
        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik sa emailom " + email + " nije pronađen."));

        // 2. Vraćamo onaj naš CustomUserDetails
        return new CustomUserDetails(korisnik);
    }


}