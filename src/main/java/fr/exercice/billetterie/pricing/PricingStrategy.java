package fr.exercice.billetterie.pricing;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateUnitPrice(BigDecimal basePrice);
}
