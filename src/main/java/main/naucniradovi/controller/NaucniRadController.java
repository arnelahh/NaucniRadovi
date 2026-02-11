package main.naucniradovi.controller;

import main.naucniradovi.model.Komentar;
import main.naucniradovi.model.Korisnik;
import main.naucniradovi.model.NaucniRad;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import main.naucniradovi.service.KomentarService;
import main.naucniradovi.service.KorisnikService;
import main.naucniradovi.service.NaucniRadService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

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
    @GetMapping("/")
    public String pocetna(@RequestParam(required = false) String pretraga,
                          @RequestParam(required = false) String sort,
                          Model model) {
        List<NaucniRad> radovi;

        if (pretraga != null && !pretraga.isEmpty()) {
            radovi = naucniRadService.univerzalnaPretraga(pretraga);
        } else {
            radovi = naucniRadService.sviRadovi();
        }

        radovi = naucniRadService.sortirajRadove(radovi, sort);
        model.addAttribute("radovi", radovi);
        model.addAttribute("pretraga", pretraga);
        model.addAttribute("sort", sort);

        return "index";
    }
    @GetMapping("/radovi/novi")
    public String noviRadForma(Model model) {
        model.addAttribute("rad", new NaucniRad());
        return "forma_rad";
    }
    @PostMapping("/radovi/sacuvaj")
    public String sacuvajRad(@Valid @ModelAttribute("rad") NaucniRad rad,
                             BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            return "forma_rad";
        }
        String email = principal.getName();
        Korisnik autor = korisnikService.nadjiPoEmailu(email).get();

        rad.setAutor(autor);
        naucniRadService.sacuvajRad(rad);

        return "redirect:/?radSacuvan=1";
    }
    @GetMapping("/radovi/pregled/{id}")
    public String pregledRada(@PathVariable Long id, Model model) {
        NaucniRad rad = naucniRadService.nadjiRadPoIdu(id);
        model.addAttribute("rad", rad);
        model.addAttribute("komentari", komentarService.komentariZaRad(rad));
        return "detalji_rada";
    }
    @GetMapping("/radovi/obrisi/{id}")
    public String obrisiRad(@PathVariable Long id, Principal principal) {
        NaucniRad rad = naucniRadService.nadjiRadPoIdu(id);
        if (!mozeUreditiRad(rad, principal)) {
            return "redirect:/radovi/pregled/" + id;
        }
        naucniRadService.obrisiRad(id);
        return "redirect:/";
    }
    @PostMapping("/radovi/{id}/komentar")
    public String dodajKomentar(@PathVariable Long id, @RequestParam String tekst, Principal principal) {
        NaucniRad rad = naucniRadService.nadjiRadPoIdu(id);
        String email = principal.getName();
        Korisnik autor = korisnikService.nadjiPoEmailu(email).get();

        komentarService.dodajKomentar(tekst, rad, autor);

        return "redirect:/radovi/pregled/" + id;
    }

    @GetMapping("/radovi/uredi/{id}")
    public String urediRad(@PathVariable Long id, Model model, Principal principal) {
        NaucniRad rad = naucniRadService.nadjiRadPoIdu(id);
        if (!mozeUreditiRad(rad, principal)) {
            return "redirect:/radovi/pregled/" + id;
        }
        model.addAttribute("rad", rad);
        model.addAttribute("editMode", true);
        model.addAttribute("akcija", "/radovi/azuriraj/" + id);
        model.addAttribute("naslovForme", "Uredi naucni rad");
        model.addAttribute("dugmeTekst", "Sacuvaj izmjene");
        return "forma_rad";
    }

    @PostMapping("/radovi/azuriraj/{id}")
    public String azurirajRad(@PathVariable Long id,
                              @Valid @ModelAttribute("rad") NaucniRad formRad,
                              BindingResult bindingResult,
                              Principal principal,
                              Model model) {
        NaucniRad postojeci = naucniRadService.nadjiRadPoIdu(id);
        if (!mozeUreditiRad(postojeci, principal)) {
            return "redirect:/radovi/pregled/" + id;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            model.addAttribute("akcija", "/radovi/azuriraj/" + id);
            model.addAttribute("naslovForme", "Uredi naucni rad");
            model.addAttribute("dugmeTekst", "Sacuvaj izmjene");
            return "forma_rad";
        }
        postojeci.setNaslov(formRad.getNaslov());
        postojeci.setSazetak(formRad.getSazetak());
        postojeci.setSadrzaj(formRad.getSadrzaj());
        naucniRadService.sacuvajRad(postojeci);
        return "redirect:/radovi/pregled/" + id;
    }

    private boolean mozeUreditiRad(NaucniRad rad, Principal principal) {
        if (principal == null) {
            return false;
        }
        if (isAdmin()) {
            return true;
        }
        return rad.getAutor().getEmail().equals(principal.getName());
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




