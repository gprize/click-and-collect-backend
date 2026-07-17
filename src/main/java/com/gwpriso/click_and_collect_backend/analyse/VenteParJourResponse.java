package com.gwpriso.click_and_collect_backend.analyse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VenteParJourResponse(
        LocalDate date,
        BigDecimal total
) {
}