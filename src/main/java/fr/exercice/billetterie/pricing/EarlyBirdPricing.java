package fr.exercice.billetterie.pricing;

import fr.exercice.billetterie.config.BilletterieProperties;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class EarlyBirdPricing implements PricingStrategy {

    private final BilletterieProperties properties;

    public EarlyBirdPricing(BilletterieProperties properties) {
        this.properties = properties;
    }

    @Override
    public BigDecimal calculateUnitPrice(BigDecimal basePrice) {
        return basePrice.multiply(BigDecimal.ONE.subtract(properties.tauxEarlybird()));
    }
}
