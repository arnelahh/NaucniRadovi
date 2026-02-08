package main.naucniradovi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ime je obavezno")
    @Size(min = 2, max = 50)
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    @Size(min = 2, max = 50)
    private String prezime;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Format emaila nije ispravan")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 6, message = "Lozinka mora imati najmanje 6 karaktera")
    private String lozinka;

    @Column(name = "uloga")
    private String uloga;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NaucniRad> radovi = new ArrayList<>();

    // Relacija 1:N (Jedan korisnik može napisati više komentara)
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Komentar> komentari = new ArrayList<>();
}
