package pl.kea.parafia.osoba;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KonwerterRoliWRodzinie implements Converter<String, RolaWRodzinie> {

    @Override
    public RolaWRodzinie convert(String zrodlo) {
        if (zrodlo == null || zrodlo.isBlank()) {
            return null;
        }
        return RolaWRodzinie.valueOf(zrodlo);
    }
}
