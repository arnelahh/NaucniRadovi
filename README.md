# NaucniRadovi - dokumentacija

## Sta je projekat
NaucniRadovi je web aplikacija za objavu, pretragu i pregled naucnih radova. Korisnici mogu da se registruju, objave radove, komentarisu tudje radove i upravljaju sopstvenim sadrzajem. Administratori imaju dodatne privilegije za upravljanje ulogama korisnika i moderaciju.

## Zasto postoji
Cilj je centralizovana i pregledna baza naucnih radova sa osnovnom kolaboracijom (komentari) i jasnom kontrolom pristupa (ulogovani korisnici i admin uloga).

## Kako radi (tehnologije i arhitektura)
- Backend: Spring Boot 4, Spring MVC, Spring Data JPA, Spring Security.
- Frontend: Thymeleaf templating + Bootstrap 5.
- Baza: MySQL (JDBC driver).
- Arhitektura: klasicni MVC sa slojevima:
  - Controller sloj: HTTP rute i View modeli.
  - Service sloj: poslovna logika.
  - Repository sloj: pristup bazi preko JPA.
  - Model sloj: entiteti i validaciona pravila.

## Struktura koda
- `src/main/java/main/naucniradovi/NaucniRadoviApplication.java`
  - Ulazna tacka i seed admin naloga (ako ne postoji).
- `src/main/java/main/naucniradovi/controller/`
  - `NaucniRadController`: lista radova, pretraga, detalji, CRUD radova, komentari.
  - `KomentarController`: uredjivanje i brisanje komentara.
  - `AuthController`: registracija i login.
  - `AdminController`: administracija uloga.
- `src/main/java/main/naucniradovi/service/`
  - `NaucniRadService`, `KomentarService`, `KorisnikService`: poslovna logika.
  - `CustomUserDetailsService`: integracija sa Spring Security.
- `src/main/java/main/naucniradovi/repository/`
  - JPA repozitorijumi za Rad, Korisnik i Komentar.
- `src/main/java/main/naucniradovi/model/`
  - `Korisnik`, `NaucniRad`, `Komentar` i `CustomUserDetails`.
- `src/main/resources/templates/`
  - Thymeleaf stranice (layout, pocetna, detalji, forma, login, registracija, admin).

## Glavne funkcionalnosti
- Registracija i prijava korisnika.
- Objavljivanje, uredjivanje i brisanje naucnih radova (autor ili admin).
- Pretraga po naslovu ili imenu autora.
- Komentari na radove (dodavanje, uredjivanje, brisanje).
- Admin panel za promjenu uloga korisnika.

## Model podataka (entiteti)
- `Korisnik`
  - Polja: ime, prezime, email (unique), lozinka, uloga.
  - Relacije: 1:N sa `NaucniRad` i 1:N sa `Komentar`.
- `NaucniRad`
  - Polja: naslov, sazetak, sadrzaj, datumObjave.
  - Relacije: N:1 ka `Korisnik` (autor), 1:N ka `Komentar`.
- `Komentar`
  - Polja: tekst, datumKreiranja.
  - Relacije: N:1 ka `NaucniRad`, N:1 ka `Korisnik`.

## Sigurnost i uloge
- Uloge: `ROLE_USER`, `ROLE_ADMIN`.
- Pristup:
  - Javne rute: `/`, `/login`, `/registracija` i staticki resursi.
  - Autentifikacija: potrebna za objavu/izmjenu/brisanje radova i komentara.
  - Admin rute: `/admin/**`.
- Seed admin nalog (ako ne postoji):
  - Email: `admin@admin.ba`
  - Lozinka: `admin`

## HTTP rute (glavne)
- `/` - pocetna + pretraga.
- `/radovi/novi` - forma za novi rad.
- `/radovi/sacuvaj` - cuvanje novog rada (POST).
- `/radovi/pregled/{id}` - detalji rada.
- `/radovi/uredi/{id}` / `/radovi/azuriraj/{id}` - izmjena rada.
- `/radovi/obrisi/{id}` - brisanje rada.
- `/radovi/{id}/komentar` - dodavanje komentara (POST).
- `/komentari/uredi/{id}` - izmjena komentara (POST).
- `/komentari/obrisi/{id}` - brisanje komentara (POST).
- `/login`, `/registracija` - autentifikacija.
- `/admin/korisnici` - admin panel.

## UI (Thymeleaf stranice)
- `layout.html`: zajednicki layout i navigacija (prikaz korisnika i admin badge).
- `index.html`: hero pretraga + lista radova.
- `detalji_rada.html`: detalji rada i komentari.
- `forma_rad.html`: objava/izmjena rada.
- `login.html`, `registracija.html`: auth UI.
- `admin_korisnici.html`: admin upravljanje ulogama.

## Konfiguracija baze
Konfiguracija je u `src/main/resources/application.properties`:
- `spring.datasource.url=jdbc:mysql://localhost:3306/naucni_radovi_db?...`
- `spring.datasource.username=...`
- `spring.datasource.password=...`
- `spring.jpa.hibernate.ddl-auto=update`

## Pokretanje lokalno
1) Pokrenuti MySQL Workbench i napraviti bazu `naucni_radovi_db`.
2) Provjeriti u `src/main/resources/application.properties` da se `spring.datasource.username` i `spring.datasource.password` slazu sa podacima baze u Workbench-u.
3) Pokrenuti aplikaciju iz IntelliJ-a preko `NaucniRadoviApplication`.
4) U browseru otvoriti `http://localhost:8080` (8080 je port).

## Napomene i ogranicenja
- Sadrzaj rada je tekst (nema upload fajlova).
- Nema paginacije ni naprednog filtriranja.
- Validacija je definisana na entitetima (polja ne smiju biti prazna).
