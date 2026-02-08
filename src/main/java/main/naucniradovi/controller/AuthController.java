package main.naucniradovi.controller;

import jakarta.validation.Valid;
import main.naucniradovi.model.Korisnik;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import main.naucniradovi.service.KorisnikService;

@Controller
public class AuthController {
    private final KorisnikService korisnikService;


    public AuthController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    // Prikaz forme za login
    @GetMapping("/login")
    public String login() {
        return "login"; // Tražit će login.html
    }

    // Prikaz forme za registraciju
    @GetMapping("/registracija")
    public String prikaziRegistraciju(Model model) {
        model.addAttribute("korisnik", new Korisnik());
        return "registracija"; // Tražit će registracija.html
    }

    // Obrada registracije
    @PostMapping("/registracija")
    public String registrujKorisnika(@Valid @ModelAttribute("korisnik") Korisnik korisnik, BindingResult result, Model model) {

        // OVO JE NOVO: Ispis u konzolu da vidimo šta se dešava
        System.out.println("--- PRIMIO SAM ZAHTJEV ZA REGISTRACIJU ---");
        System.out.println("Ime: " + korisnik.getIme());
        System.out.println("Email: " + korisnik.getEmail());
        System.out.println("Lozinka: " + korisnik.getLozinka());

        // Ako ima grešaka u validaciji (npr. prazno ime)
        if (result.hasErrors()) {
            return "registracija";
        }

        try {
            korisnikService.registrujKorisnika(korisnik);
        } catch (RuntimeException e) {
            model.addAttribute("greska", e.getMessage()); // Npr. "Email već postoji"
            return "registracija";
        }

        return "redirect:/login?uspjesno"; // Prebaci na login nakon uspjeha
    }
}
