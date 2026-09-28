document.querySelectorAll("[data-potwierdz]").forEach((formularz) => {
    formularz.addEventListener("submit", (zdarzenie) => {
        if (!window.confirm(formularz.dataset.potwierdz)) {
            zdarzenie.preventDefault();
        }
    });
});

document.querySelectorAll("[data-filtr]").forEach((pole) => {
    const cel = document.getElementById(pole.dataset.filtr);
    if (!cel) {
        return;
    }
    pole.addEventListener("input", () => {
        const szukane = pole.value.trim().toLocaleLowerCase("pl");
        Array.from(cel.options).forEach((opcja) => {
            const tekst = opcja.text.toLocaleLowerCase("pl");
            opcja.hidden = szukane.length > 0 && !tekst.includes(szukane);
        });
    });
});

document.querySelectorAll("[data-filtr-wierszy]").forEach((pole) => {
    const tabela = document.getElementById(pole.dataset.filtrWierszy);
    if (!tabela) {
        return;
    }
    pole.addEventListener("input", () => {
        const szukane = pole.value.trim().toLocaleLowerCase("pl");
        tabela.querySelectorAll("tbody tr").forEach((wiersz) => {
            const tekst = wiersz.innerText.toLocaleLowerCase("pl");
            wiersz.hidden = szukane.length > 0 && !tekst.includes(szukane);
        });
    });
});

document.querySelectorAll("[data-zaznacz-wszystkie]").forEach((przycisk) => {
    przycisk.addEventListener("click", () => {
        const grupa = document.getElementById(przycisk.dataset.zaznaczWszystkie);
        if (!grupa) {
            return;
        }
        grupa.querySelectorAll('input[type="checkbox"]').forEach((pole) => {
            pole.checked = true;
        });
    });
});
