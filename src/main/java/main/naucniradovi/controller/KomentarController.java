package main.naucniradovi.controller;

import main.naucniradovi.model.Komentar;
import main.naucniradovi.service.KomentarService;
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

        // Provjera: Da li je onaj ko briše isti onaj ko je napisao komentar?
        if (k.getAutor().getEmail().equals(emailUlogovanog)) {
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

        if (k.getAutor().getEmail().equals(emailUlogovanog)) {
            komentarService.izmjeniKomentar(id, noviTekst);

        } else {
            System.out.println("Pokušaj izmjene tuđeg komentara od strane: " + emailUlogovanog);
        }
        return "redirect:/radovi/pregled/" + k.getRad().getId();
    }
}
