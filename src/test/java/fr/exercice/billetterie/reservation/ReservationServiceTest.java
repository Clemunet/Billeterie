package fr.exercice.billetterie.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.exercice.billetterie.config.BilletterieProperties;
import fr.exercice.billetterie.event.Event;
import fr.exercice.billetterie.event.EventRepository;
import fr.exercice.billetterie.pricing.PricingService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest {

    private EventRepository eventRepository;
    private ReservationRepository reservationRepository;
    private PricingService pricingService;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        pricingService = mock(PricingService.class);
        service = new ReservationService(eventRepository, reservationRepository,
                pricingService, new BilletterieProperties(6, new BigDecimal("0.10"), 30));
    }

    @Test
    void bookingWithinCapacityReturnsTotalPrice() {
        Event event = eventWithCapacity(10);
        when(eventRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(event));
        when(pricingService.calculateUnitPrice(any(), any())).thenReturn(new BigDecimal("90.00"));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = service.book(1L, "client@example.com", 3);

        assertEquals(0, new BigDecimal("270.00").compareTo(reservation.getTotalPrice()));
        assertEquals(7, event.availablePlaces());
    }

    @Test
    void bookingBeyondRemainingPlacesIsRejected() {
        Event event = eventWithCapacity(2);
        when(eventRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(event));

        assertThrows(NotEnoughPlacesException.class,
                () -> service.book(1L, "client@example.com", 3));
        assertEquals(2, event.availablePlaces());
    }

    @Test
    void bookingBeyondConfiguredLimitIsRejected() {
        Event event = eventWithCapacity(10);
        when(eventRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(event));

        assertThrows(InvalidReservationException.class,
                () -> service.book(1L, "client@example.com", 7));
        assertEquals(10, event.availablePlaces());
    }

    private Event eventWithCapacity(int capacity) {
        return new Event("Concert", LocalDateTime.now().plusDays(40),
                capacity, new BigDecimal("100.00"));
    }
}
