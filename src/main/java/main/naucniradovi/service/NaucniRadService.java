package main.naucniradovi.service;

import main.naucniradovi.model.Korisnik;
import main.naucniradovi.model.NaucniRad;
import org.springframework.stereotype.Service;
import main.naucniradovi.repository.NaucniRadRepository;

import java.util.List;

@Service
public class NaucniRadService {
    private final NaucniRadRepository naucniRadRepository;

    public NaucniRadService(NaucniRadRepository naucniRadRepository) {
        this.naucniRadRepository = naucniRadRepository;
    }

    public List<NaucniRad> sviRadovi() {
        return naucniRadRepository.findAll();
    }

    public List<NaucniRad> radoviAutora(Korisnik autor) {
        return naucniRadRepository.findByAutor(autor);
    }

    public NaucniRad nadjiRadPoIdu(Long id) {
        return naucniRadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rad nije pronađen sa ID: " + id));
    }

    public List<NaucniRad> nadjiPoNaslovu(String naslov) {
        if (naslov == null || naslov.isEmpty()) {
            return naucniRadRepository.findAll(); // Ako korisnik ne ukuca ništa, vrati sve
        }
        return naucniRadRepository.findByNaslovContainingIgnoreCase(naslov);
    }

    public List<NaucniRad> pretragaPoAutoru(String imeAutora) {
        if (imeAutora == null || imeAutora.trim().isEmpty()) {
            return naucniRadRepository.findAll(); // Ako ništa ne upiše, vrati sve
        }
        return naucniRadRepository.findByAutorImeContainingIgnoreCase(imeAutora);
    }

    public List<NaucniRad> univerzalnaPretraga(String pojam) {
        if (pojam == null || pojam.isEmpty()) {
            return naucniRadRepository.findAll();
        }
        // Šaljemo isti pojam dvaput: "traži ovaj tekst u naslovu ILI u autoru"
        return naucniRadRepository.findByNaslovContainingIgnoreCaseOrAutorImeContainingIgnoreCase(pojam, pojam);
    }

    public void sacuvajRad(NaucniRad rad) {
        naucniRadRepository.save(rad);
    }

    public void obrisiRad(Long id) {
        naucniRadRepository.deleteById(id);
    }
}
