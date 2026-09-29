# Kancelaria parafii ewangelicko-augsburskiej

Aplikacja biura parafialnego dla fikcyjnej Parafii Ewangelicko-Augsburskiej Zmiłowania Bożego w Poznaniu (Diecezja Pomorsko-Wielkopolska).

Moduły: kartoteka i gospodarstwa, księgi chrztów, konfirmacji, ślubów, zgonów i przeniesień, grupy, kalendarz, obiekty parafii (salki, hotel, pokoje gościnne i ich rezerwacje), porządek nabożeństw, obecności, dyżury i zadania, finanse, poufne duszpasterstwo, cele parafii i nauka konfirmacyjna, ogłoszenia oraz raporty CSV.

Tablica ogłoszeń bez logowania: [http://localhost:8080/ogloszenia-publiczne](http://localhost:8080/ogloszenia-publiczne).

## Uruchomienie

Wymagana jest Java 21.

```bash
./mvnw spring-boot:run
```

Aplikacja startuje na [http://localhost:8080](http://localhost:8080). Baza robocza to plik H2 w katalogu `data/`.

W trybie demonstracyjnym hasło wszystkich kont to `poznan#2026`.

| E-mail | Rola |
| --- | --- |
| admin@poznan.parafia.pl | Administrator |
| proboszcz@poznan.parafia.pl | Proboszcz |
| biuro@poznan.parafia.pl | Sekretariat |
| skarbnik@poznan.parafia.pl | Skarbnik |
| duszpasterz@poznan.parafia.pl | Duszpasterz |

## Testy

```bash
./mvnw test
```

## Profil PostgreSQL

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Najpierw uruchom bazę: `docker compose up -d`. Zmienne środowiskowe: `PARAFIA_DB_URL`, `PARAFIA_DB_USER`, `PARAFIA_DB_PASSWORD`. Domyślne połączenie to `jdbc:postgresql://localhost:5432/parafia`, użytkownik i hasło `parafia`.
