package fr.exercice.billetterie.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.exercice.billetterie.config.BilletterieProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PricingServiceTest {

    private final BilletterieProperties properties =
            new BilletterieProperties(6, new BigDecimal("0.10"), 30);
    private final PricingService service = new PricingService(
            new StandardPricing(), new EarlyBirdPricing(properties), properties);

    @Test
    void eventAfterThirtyDaysUsesEarlyBirdRate() {
        BigDecimal price = service.calculateUnitPrice(
                new BigDecimal("100.00"), LocalDate.now().plusDays(31));

        assertEquals(0, new BigDecimal("90.00").compareTo(price));
    }

    @Test
    void eventAtThirtyDaysUsesStandardRate() {
        BigDecimal price = service.calculateUnitPrice(
                new BigDecimal("100.00"), LocalDate.now().plusDays(30));

        assertEquals(0, new BigDecimal("100.00").compareTo(price));
    }
}
