package main.naucniradovi.controller;

import main.naucniradovi.model.Komentar;
import main.naucniradovi.service.KomentarService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class KomentarController {

    private final KomentarService komentarService;

    public KomentarController(KomentarService komentarService) {
        this.komentarService = komentarService;
    }

    @PostMapping("/komentari/obrisi/{id}")
    public String obrisi(@PathVariable Long id, Principal principal) {
        Komentar k = komentarService.nadjiKomentar(id);
        String emailUlogovanog = principal.getName();

        // Provjera: Da li je onaj ko brise isti onaj ko je napisao komentar?
        if (k.getAutor().getEmail().equals(emailUlogovanog) || isAdmin()) {
            komentarService.obrisiKomentar(id);
        }

        return "redirect:/radovi/pregled/" + k.getRad().getId();
    }

    @PostMapping("/komentari/uredi/{id}")
    public String urediKomentar(@PathVariable Long id,
                                @RequestParam String noviTekst,
                                Principal principal) {

        Komentar k = komentarService.nadjiKomentar(id);
        String emailUlogovanog = principal.getName();

        if (k.getAutor().getEmail().equals(emailUlogovanog) || isAdmin()) {
            komentarService.izmjeniKomentar(id, noviTekst);

        } else {
            System.out.println("Pokusaj izmjene tudjeg komentara od strane: " + emailUlogovanog);
        }
        return "redirect:/radovi/pregled/" + k.getRad().getId();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
