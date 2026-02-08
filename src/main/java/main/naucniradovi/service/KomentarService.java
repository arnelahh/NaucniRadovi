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

    // Dodavanje komentara
    public void dodajKomentar(String tekst, NaucniRad rad, Korisnik autor) {
        Komentar komentar = new Komentar();
        komentar.setTekst(tekst);
        komentar.setRad(rad);
        komentar.setAutor(autor);
        // Datum se postavlja automatski zbog @CreationTimestamp u modelu

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

    // 2. Izmjeni tekst komentara
    public void izmjeniKomentar(Long id, String noviTekst) {
        Komentar k = nadjiKomentar(id); // Prvo ga nađemo
        k.setTekst(noviTekst);          // Promijenimo tekst
        komentarRepository.save(k);     // Spasimo promjenu
    }

    // 3. Obriši komentar
    public void obrisiKomentar(Long id) {
        if (komentarRepository.existsById(id)) {
            komentarRepository.deleteById(id);
        } else {
            throw new RuntimeException("Pokušavate obrisati komentar koji ne postoji!");
        }
    }
}
