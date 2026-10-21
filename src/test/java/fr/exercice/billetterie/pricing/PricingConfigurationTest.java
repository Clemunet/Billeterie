package fr.exercice.billetterie.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.exercice.billetterie.config.BilletterieProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PricingConfigurationTest {

    @Autowired
    private BilletterieProperties properties;

    @Autowired
    private PricingService pricingService;

    @Test
    void defaultPropertiesAreLoaded() {
        assertEquals(6, properties.maxPlacesParReservation());
        assertEquals(0, new BigDecimal("0.10").compareTo(properties.tauxEarlybird()));
        assertEquals(30, properties.seuilEarlybirdJours());
    }

    @Test
    void eventAfterThresholdUsesEarlyBirdRate() {
        BigDecimal price = pricingService.calculateUnitPrice(
                new BigDecimal("100.00"), LocalDate.now().plusDays(31));

        assertEquals(0, new BigDecimal("90.00").compareTo(price));
    }

    @Test
    void eventAtThresholdUsesStandardRate() {
        BigDecimal price = pricingService.calculateUnitPrice(
                new BigDecimal("100.00"), LocalDate.now().plusDays(30));

        assertEquals(0, new BigDecimal("100.00").compareTo(price));
    }
}
