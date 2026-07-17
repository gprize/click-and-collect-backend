package com.gwpriso.click_and_collect_backend.analyse;

import java.math.BigDecimal;

public record JourSemaineVenteResponse(
        String jour,
        BigDecimal total
) {
}