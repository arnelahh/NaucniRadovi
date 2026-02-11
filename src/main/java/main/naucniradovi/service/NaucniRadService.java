package main.naucniradovi.service;

import main.naucniradovi.model.Korisnik;
import main.naucniradovi.model.NaucniRad;
import org.springframework.stereotype.Service;
import main.naucniradovi.repository.KomentarRepository;
import main.naucniradovi.repository.NaucniRadRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaucniRadService {
    private final NaucniRadRepository naucniRadRepository;
    private final KomentarRepository komentarRepository;

    public NaucniRadService(NaucniRadRepository naucniRadRepository, KomentarRepository komentarRepository) {
        this.naucniRadRepository = naucniRadRepository;
        this.komentarRepository = komentarRepository;
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
            return naucniRadRepository.findAll();
        }
        return naucniRadRepository.findByNaslovContainingIgnoreCase(naslov);
    }

    public List<NaucniRad> pretragaPoAutoru(String imeAutora) {
        if (imeAutora == null || imeAutora.trim().isEmpty()) {
            return naucniRadRepository.findAll();
        }
        return naucniRadRepository.findByAutorImeContainingIgnoreCase(imeAutora);
    }

    public List<NaucniRad> univerzalnaPretraga(String pojam) {
        if (pojam == null || pojam.isEmpty()) {
            return naucniRadRepository.findAll();
        }
        return naucniRadRepository.findByNaslovContainingIgnoreCaseOrAutorImeContainingIgnoreCase(pojam, pojam);
    }

    public void sacuvajRad(NaucniRad rad) {
        naucniRadRepository.save(rad);
    }

    public void obrisiRad(Long id) {
        naucniRadRepository.deleteById(id);
    }

    public List<NaucniRad> sortirajRadove(List<NaucniRad> radovi, String sort) {
        if (sort == null || sort.isBlank()) {
            return radovi;
        }
        List<NaucniRad> sortirani = new ArrayList<>(radovi);
        switch (sort) {
            case "najnovije" -> sortirani.sort(Comparator.comparing(NaucniRad::getDatumObjave).reversed());
            case "najstarije" -> sortirani.sort(Comparator.comparing(NaucniRad::getDatumObjave));
            case "naslov" -> sortirani.sort(Comparator.comparing(rad -> rad.getNaslov().toLowerCase()));
            case "popularno" -> {
                Map<Long, Long> brojeviKomentara = new HashMap<>();
                for (NaucniRad rad : sortirani) {
                    brojeviKomentara.put(rad.getId(), komentarRepository.countByRad(rad));
                }
                sortirani.sort((a, b) -> {
                    long countA = brojeviKomentara.getOrDefault(a.getId(), 0L);
                    long countB = brojeviKomentara.getOrDefault(b.getId(), 0L);
                    if (countA == countB) {
                        return b.getDatumObjave().compareTo(a.getDatumObjave());
                    }
                    return Long.compare(countB, countA);
                });
            }
            default -> {
            }
        }
        return sortirani;
    }
}

