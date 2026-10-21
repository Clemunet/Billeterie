package fr.exercice.billetterie.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.exercice.billetterie.config.BilletterieProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
class DevPricingConfigurationTest {

    @Autowired
    private BilletterieProperties properties;

    @Autowired
    private PricingService pricingService;

    @Test
    void devProfileOverridesEarlyBirdRate() {
        assertEquals(0, new BigDecimal("0.50").compareTo(properties.tauxEarlybird()));

        BigDecimal price = pricingService.calculateUnitPrice(
                new BigDecimal("100.00"), LocalDate.now().plusDays(31));
        assertEquals(0, new BigDecimal("50.00").compareTo(price));
    }
}
