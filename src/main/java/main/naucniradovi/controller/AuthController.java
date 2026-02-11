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
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/registracija")
    public String prikaziRegistraciju(Model model) {
        model.addAttribute("korisnik", new Korisnik());
        return "registracija";
    }
    @PostMapping("/registracija")
    public String registrujKorisnika(@Valid @ModelAttribute("korisnik") Korisnik korisnik, BindingResult result, Model model) {
        System.out.println("--- PRIMIO SAM ZAHTJEV ZA REGISTRACIJU ---");
        System.out.println("Ime: " + korisnik.getIme());
        System.out.println("Email: " + korisnik.getEmail());
        System.out.println("Lozinka: " + korisnik.getLozinka());
        if (result.hasErrors()) {
            return "registracija";
        }

        try {
            korisnikService.registrujKorisnika(korisnik);
        } catch (RuntimeException e) {
            model.addAttribute("greska", e.getMessage());
            return "registracija";
        }

        return "redirect:/login?uspjesno";
    }
}

