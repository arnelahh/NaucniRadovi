package main.naucniradovi.repository;

import main.naucniradovi.model.Komentar;
import main.naucniradovi.model.NaucniRad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KomentarRepository extends JpaRepository<Komentar, Long> {
    List<Komentar> findByRad(NaucniRad rad);
}
