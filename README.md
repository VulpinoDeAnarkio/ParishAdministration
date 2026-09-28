# Kancelaria parafii ewangelicko-augsburskiej

Aplikacja biura parafialnego dla fikcyjnej Parafii Ewangelicko-Augsburskiej Świętej Trójcy w Lipowej.

Moduły: kartoteka i gospodarstwa, księgi chrztów, konfirmacji, ślubów, zgonów i przeniesień, grupy, kalendarz z rezerwacją sal, porządek nabożeństw, obecności, dyżury i zadania, finanse, poufne duszpasterstwo, cele parafii i nauka konfirmacyjna, ogłoszenia oraz raporty CSV.

Tablica ogłoszeń bez logowania: [http://localhost:8080/ogloszenia-publiczne](http://localhost:8080/ogloszenia-publiczne).

## Uruchomienie

Wymagana jest Java 21.

```bash
./mvnw spring-boot:run
```

Aplikacja startuje na [http://localhost:8080](http://localhost:8080). Baza robocza to plik H2 w katalogu `data/`.

W trybie demonstracyjnym hasło wszystkich kont to `Lipowa#2026`.

| E-mail | Rola |
| --- | --- |
| admin@lipowa.parafia.pl | Administrator |
| proboszcz@lipowa.parafia.pl | Proboszcz |
| biuro@lipowa.parafia.pl | Sekretariat |
| skarbnik@lipowa.parafia.pl | Skarbnik |
| duszpasterz@lipowa.parafia.pl | Duszpasterz |

## Testy

```bash
./mvnw test
```

## Profil PostgreSQL

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Najpierw uruchom bazę: `docker compose up -d`. Zmienne środowiskowe: `PARAFIA_DB_URL`, `PARAFIA_DB_USER`, `PARAFIA_DB_PASSWORD`. Domyślne połączenie to `jdbc:postgresql://localhost:5432/parafia`, użytkownik i hasło `parafia`.
