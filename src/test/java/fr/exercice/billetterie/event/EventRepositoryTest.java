package fr.exercice.billetterie.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository repository;

    @Test
    void derivedQueryReturnsOnlyUpcomingEventsInDateOrder() {
        LocalDateTime now = LocalDateTime.now();
        repository.save(new Event("Plus tard", now.plusDays(20), 10, BigDecimal.TEN));
        repository.save(new Event("Passé", now.minusDays(1), 10, BigDecimal.TEN));
        repository.save(new Event("Bientôt", now.plusDays(5), 10, BigDecimal.TEN));

        List<String> names = repository.findByDateAfterOrderByDateAsc(now).stream()
                .map(Event::getName)
                .toList();

        assertEquals(List.of("Bientôt", "Plus tard"), names);
    }
}
