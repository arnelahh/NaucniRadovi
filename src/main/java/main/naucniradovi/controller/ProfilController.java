package main.naucniradovi.controller;

import main.naucniradovi.model.Korisnik;
import main.naucniradovi.service.KomentarService;
import main.naucniradovi.service.KorisnikService;
import main.naucniradovi.service.NaucniRadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfilController {
    private final KorisnikService korisnikService;
    private final NaucniRadService naucniRadService;
    private final KomentarService komentarService;

    public ProfilController(KorisnikService korisnikService, NaucniRadService naucniRadService, KomentarService komentarService) {
        this.korisnikService = korisnikService;
        this.naucniRadService = naucniRadService;
        this.komentarService = komentarService;
    }

    @GetMapping("/profil")
    public String profil(Model model, Principal principal) {
        Korisnik korisnik = korisnikService.nadjiPoEmailu(principal.getName())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronadjen."));
        model.addAttribute("korisnik", korisnik);
        model.addAttribute("radovi", naucniRadService.radoviAutora(korisnik));
        model.addAttribute("komentari", komentarService.komentariAutora(korisnik));
        return "profil";
    }
}
