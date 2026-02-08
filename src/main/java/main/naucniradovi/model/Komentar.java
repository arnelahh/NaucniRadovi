package main.naucniradovi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Komentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Komentar ne može biti prazan")
    @Size(max = 500, message = "Komentar ne smije biti duži od 500 karaktera")
    private String tekst;

    @CreationTimestamp
    private LocalDateTime datumKreiranja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rad_id", nullable = false)
    private NaucniRad rad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Korisnik autor;
}

