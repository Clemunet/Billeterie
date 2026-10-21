package fr.exercice.billetterie.pricing;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private final PricingStrategy strategy;

    public PricingService(@Qualifier("standardPricing") PricingStrategy strategy) {
        this.strategy = strategy;
    }
}
