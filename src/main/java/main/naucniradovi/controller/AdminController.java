package main.naucniradovi.controller;

import main.naucniradovi.service.KorisnikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    private final KorisnikService korisnikService;

    public AdminController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping("/admin/korisnici")
    public String korisnici(Model model) {
        model.addAttribute("korisnici", korisnikService.sviKorisnici());
        model.addAttribute("uloge", List.of("ROLE_USER", "ROLE_ADMIN"));
        return "admin_korisnici";
    }

    @PostMapping("/admin/korisnici/{id}/uloga")
    public String promijeniUlogu(@PathVariable Long id, @RequestParam String uloga) {
        korisnikService.promijeniUlogu(id, uloga);
        return "redirect:/admin/korisnici";
    }

    @GetMapping("/admin_korisnici")
    public String korisniciAlias() {
        return "redirect:/admin/korisnici";
    }
}
