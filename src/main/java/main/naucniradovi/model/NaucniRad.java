package main.naucniradovi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaucniRad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Naslov je obavezan")
    @Size(min = 5, max = 200, message = "Naslov mora imati između 5 i 200 karaktera")
    private String naslov;

    @NotBlank(message = "Sažetak je obavezan")
    @Column(columnDefinition = "TEXT")
    private String sazetak;

    @NotBlank(message = "Sadržaj rada je obavezan")
    @Column(columnDefinition = "TEXT")
    private String sadrzaj;

    @CreationTimestamp
    private LocalDateTime datumObjave;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Korisnik autor;

    @OneToMany(mappedBy = "rad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Komentar> komentari = new ArrayList<>();
}

