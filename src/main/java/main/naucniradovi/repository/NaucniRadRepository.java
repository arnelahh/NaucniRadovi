package main.naucniradovi.repository;


import main.naucniradovi.model.Korisnik;
import main.naucniradovi.model.NaucniRad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NaucniRadRepository extends JpaRepository<NaucniRad, Long> {
    List<NaucniRad> findByAutor(Korisnik autor);
    List<NaucniRad> findByNaslovContainingIgnoreCase(String dioNaslova);
    List<NaucniRad> findByAutorImeContainingIgnoreCase(String ime);
    List<NaucniRad> findByNaslovContainingIgnoreCaseOrAutorImeContainingIgnoreCase(String naslov, String imeAutora);
}
