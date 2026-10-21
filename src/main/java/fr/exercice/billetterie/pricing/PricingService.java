package fr.exercice.billetterie.pricing;

import fr.exercice.billetterie.config.BilletterieProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private final PricingStrategy standardPricing;
    private final PricingStrategy earlyBirdPricing;
    private final BilletterieProperties properties;

    public PricingService(
            @Qualifier("standardPricing") PricingStrategy standardPricing,
            @Qualifier("earlyBirdPricing") PricingStrategy earlyBirdPricing,
            BilletterieProperties properties
    ) {
        this.standardPricing = standardPricing;
        this.earlyBirdPricing = earlyBirdPricing;
        this.properties = properties;
    }

    public BigDecimal calculateUnitPrice(BigDecimal basePrice, LocalDate eventDate) {
        if (eventDate.isAfter(LocalDate.now().plusDays(properties.seuilEarlybirdJours()))) {
            return earlyBirdPricing.calculateUnitPrice(basePrice);
        }
        return standardPricing.calculateUnitPrice(basePrice);
    }
}
