package fr.exercice.billetterie.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class EventServiceTest {

    @Test
    void listUpcomingExcludesPastEventsAndSortsByDate() {
        EventRepository repository = mock(EventRepository.class);
        LocalDateTime now = LocalDateTime.now();
        Event past = new Event(1L, "Passé", now.minusDays(1), 10, BigDecimal.TEN);
        Event later = new Event(2L, "Plus tard", now.plusDays(20), 10, BigDecimal.TEN);
        Event sooner = new Event(3L, "Bientôt", now.plusDays(5), 10, BigDecimal.TEN);
        when(repository.findAll()).thenReturn(List.of(later, past, sooner));

        List<Event> result = new EventService(repository).listUpcoming();

        assertEquals(List.of(sooner, later), result);
    }
}
