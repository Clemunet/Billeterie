package fr.exercice.billetterie.pricing;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class EarlyBirdPricing implements PricingStrategy {

    @Override
    public BigDecimal calculateUnitPrice(BigDecimal basePrice) {
        return basePrice.multiply(new BigDecimal("0.90"));
    }
}