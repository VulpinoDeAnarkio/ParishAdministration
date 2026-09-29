UPDATE profil_parafii SET
    nazwa = 'Parafia Ewangelicko-Augsburska Zmiłowania Bożego w Poznaniu',
    diecezja = 'Diecezja Pomorsko-Wielkopolska',
    ulica = 'ul. Reformacji 12',
    kod_pocztowy = '61-100',
    miejscowosc = 'Poznań',
    telefon = '61 100 20 30',
    email = 'biuro@poznan.parafia.pl',
    www = 'https://poznan.parafia.pl',
    kurator = 'Astryda Lisienko',
    zmodyfikowano = CURRENT_TIMESTAMP;

UPDATE uzytkownik
SET email = REPLACE(email, '@lipowa.parafia.pl', '@poznan.parafia.pl'),
    zmodyfikowano = CURRENT_TIMESTAMP
WHERE email LIKE '%@lipowa.parafia.pl';

UPDATE chrzest
SET miejsce = 'Kościół Zmiłowania Bożego w Poznaniu'
WHERE miejsce = 'Kościół Świętej Trójcy w Lipowej';

UPDATE konfirmacja
SET miejsce = 'Kościół Zmiłowania Bożego w Poznaniu'
WHERE miejsce = 'Kościół Świętej Trójcy w Lipowej';

UPDATE slub
SET miejsce = 'Kościół Zmiłowania Bożego w Poznaniu'
WHERE miejsce = 'Kościół Świętej Trójcy w Lipowej';

UPDATE pogrzeb
SET miejsce = 'Cmentarz w Poznaniu'
WHERE miejsce = 'Cmentarz w Lipowej';

UPDATE pogrzeb
SET cmentarz = 'Cmentarz w Poznaniu'
WHERE cmentarz = 'Cmentarz w Lipowej';

UPDATE osoba
SET miejsce_urodzenia = 'Poznań'
WHERE miejsce_urodzenia = 'Lipowa';

UPDATE osoba
SET ulica = 'ul. Reformacji 4',
    kod_pocztowy = '61-100',
    miejscowosc = 'Poznań'
WHERE miejscowosc = 'Lipowa' OR ulica = 'Lipowa 12';

UPDATE gospodarstwo
SET ulica = 'ul. Reformacji 4',
    kod_pocztowy = '61-100',
    miejscowosc = 'Poznań'
WHERE miejscowosc = 'Lipowa' OR ulica = 'Lipowa 12';
