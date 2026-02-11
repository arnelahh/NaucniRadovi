package main.naucniradovi.service;

import main.naucniradovi.model.Komentar;
import main.naucniradovi.model.Korisnik;
import main.naucniradovi.model.NaucniRad;
import org.springframework.stereotype.Service;
import main.naucniradovi.repository.KomentarRepository;

import java.util.List;

@Service
public class KomentarService {
    private final KomentarRepository komentarRepository;

    public KomentarService(KomentarRepository komentarRepository) {
        this.komentarRepository = komentarRepository;
    }
    public void dodajKomentar(String tekst, NaucniRad rad, Korisnik autor) {
        Komentar komentar = new Komentar();
        komentar.setTekst(tekst);
        komentar.setRad(rad);
        komentar.setAutor(autor);

        komentarRepository.save(komentar);
    }

    public List<Komentar> komentariZaRad(NaucniRad rad) {
        return komentarRepository.findByRad(rad);
    }

    public List<Komentar> komentariAutora(Korisnik autor) {
        return komentarRepository.findByAutor(autor);
    }

    public Komentar nadjiKomentar(Long id) {
        return komentarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Komentar sa ID " + id + " nije pronađen!"));
    }
    public void izmjeniKomentar(Long id, String noviTekst) {
        Komentar k = nadjiKomentar(id);
        k.setTekst(noviTekst);
        komentarRepository.save(k);
    }
    public void obrisiKomentar(Long id) {
        if (komentarRepository.existsById(id)) {
            komentarRepository.deleteById(id);
        } else {
            throw new RuntimeException("Pokušavate obrisati komentar koji ne postoji!");
        }
    }
}

