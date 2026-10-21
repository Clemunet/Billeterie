package fr.exercice.billetterie.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import fr.exercice.billetterie.event.Event;
import fr.exercice.billetterie.event.EventRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReservationPersistenceTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService service;

    @Test
    void cancellationRestoresPlacesAndDeletesReservation() {
        Event event = eventRepository.save(new Event(
                "Concert", LocalDateTime.now().plusDays(10), 10, new BigDecimal("20.00")));

        Reservation reservation = service.book(event.getId(), "client@example.com", 2);

        assertEquals(8, eventRepository.findById(event.getId()).orElseThrow().availablePlaces());
        assertEquals(event.getId(), reservationRepository.findById(reservation.getId())
                .orElseThrow().getEvent().getId());

        service.cancel(event.getId(), reservation.getId());

        assertEquals(10, eventRepository.findById(event.getId()).orElseThrow().availablePlaces());
        assertFalse(reservationRepository.existsById(reservation.getId()));
    }
}
