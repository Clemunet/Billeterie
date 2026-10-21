package fr.exercice.billetterie.pricing;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class StandardPricing implements PricingStrategy {

    @Override
    public BigDecimal calculateUnitPrice(BigDecimal basePrice) {
        return basePrice;
    }
}