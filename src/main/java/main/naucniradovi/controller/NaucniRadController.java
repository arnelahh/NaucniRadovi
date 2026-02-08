package main.naucniradovi.controller;

import main.naucniradovi.model.Komentar;
import main.naucniradovi.model.Korisnik;
import main.naucniradovi.model.NaucniRad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import main.naucniradovi.service.KomentarService;
import main.naucniradovi.service.KorisnikService;
import main.naucniradovi.service.NaucniRadService;

import java.security.Principal;
import java.util.List;

@Controller
public class NaucniRadController {
    private final NaucniRadService naucniRadService;
    private final KorisnikService korisnikService;
    private final KomentarService komentarService;

    public NaucniRadController(NaucniRadService naucniRadService, KorisnikService korisnikService, KomentarService komentarService) {
        this.naucniRadService = naucniRadService;
        this.korisnikService = korisnikService;
        this.komentarService = komentarService;
    }

    // Početna stranica - lista svih radova + pretraga
    @GetMapping("/")
    public String pocetna(@RequestParam(required = false) String pretraga, Model model) {
        List<NaucniRad> radovi;

        if (pretraga != null && !pretraga.isEmpty()) {
            // Pozivamo novu pametnu pretragu (traži i po naslovu i po autoru)
            radovi = naucniRadService.univerzalnaPretraga(pretraga);
        } else {
            radovi = naucniRadService.sviRadovi();
        }

        model.addAttribute("radovi", radovi);
        // Ako želiš da tekst ostane upisan u search baru nakon pretrage:
        model.addAttribute("pretraga", pretraga);

        return "index";
    }

    // Forma za novi rad
    @GetMapping("/radovi/novi")
    public String noviRadForma(Model model) {
        model.addAttribute("rad", new NaucniRad());
        return "forma_rad"; // Tražit će forma_rad.html
    }

    // Čuvanje novog rada
    @PostMapping("/radovi/sacuvaj")
    public String sacuvajRad(@ModelAttribute NaucniRad rad, Principal principal) {
        // Principal sadrži email ulogovanog korisnika
        String email = principal.getName();
        Korisnik autor = korisnikService.nadjiPoEmailu(email).get();

        rad.setAutor(autor);
        naucniRadService.sacuvajRad(rad);

        return "redirect:/";
    }

    // Pregled detalja rada
    @GetMapping("/radovi/pregled/{id}")
    public String pregledRada(@PathVariable Long id, Model model) {
        NaucniRad rad = naucniRadService.nadjiRadPoIdu(id);
        model.addAttribute("rad", rad);
        model.addAttribute("komentari", komentarService.komentariZaRad(rad));
        return "detalji_rada"; // Tražit će detalji_rada.html
    }

    // Brisanje rada
    @GetMapping("/radovi/obrisi/{id}")
    public String obrisiRad(@PathVariable Long id) {
        naucniRadService.obrisiRad(id);
        return "redirect:/";
    }

    // Dodavanje komentara (direktno na stranici detalja)
    @PostMapping("/radovi/{id}/komentar")
    public String dodajKomentar(@PathVariable Long id, @RequestParam String tekst, Principal principal) {
        NaucniRad rad = naucniRadService.nadjiRadPoIdu(id);
        String email = principal.getName();
        Korisnik autor = korisnikService.nadjiPoEmailu(email).get();

        komentarService.dodajKomentar(tekst, rad, autor);

        return "redirect:/radovi/pregled/" + id;
    }

}
