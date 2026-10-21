package fr.exercice.billetterie.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "billetterie")
public record BilletterieProperties(
        int maxPlacesParReservation,
        BigDecimal tauxEarlybird,
        int seuilEarlybirdJours
) {
}
