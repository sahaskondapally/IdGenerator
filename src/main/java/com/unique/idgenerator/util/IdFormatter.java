package com.unique.idgenerator.util;

import org.springframework.stereotype.Component;

@Component
public class IdFormatter {

    public String formatToHex(long value) {
        return String.format("%016x", value);
    }

    public String formatId(long idLow, long idHigh) {
        return formatToHex(idLow) + formatToHex(idHigh);
    }
}
