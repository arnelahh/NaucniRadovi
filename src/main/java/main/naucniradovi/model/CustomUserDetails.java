package main.naucniradovi.model;

import main.naucniradovi.model.Korisnik;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Korisnik korisnik; // Čuvamo cijelog korisnika ovdje!

    public CustomUserDetails(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    // Ovu metodu ćemo zvati u HTML-u da dobijemo ime!
    public String getPunoIme() {
        return korisnik.getIme() + " " + korisnik.getPrezime();
    }

    // Getter za samog korisnika ako zatreba
    public Korisnik getKorisnik() {
        return korisnik;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(korisnik.getUloga()));
    }

    @Override
    public String getPassword() {
        return korisnik.getLozinka();
    }

    @Override
    public String getUsername() {
        return korisnik.getEmail();
    }

    // Ostale metode moraju vratiti true da bi nalog bio aktivan
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}