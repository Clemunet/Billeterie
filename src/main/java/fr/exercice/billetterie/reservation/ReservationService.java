package fr.exercice.billetterie.reservation;

import fr.exercice.billetterie.config.BilletterieProperties;
import fr.exercice.billetterie.event.Event;
import fr.exercice.billetterie.event.EventNotFoundException;
import fr.exercice.billetterie.event.EventRepository;
import fr.exercice.billetterie.pricing.PricingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;
    private final PricingService pricingService;
    private final BilletterieProperties properties;

    public ReservationService(
            EventRepository eventRepository,
            ReservationRepository reservationRepository,
            PricingService pricingService,
            BilletterieProperties properties
    ) {
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
        this.pricingService = pricingService;
        this.properties = properties;
    }

    @Transactional
    public Reservation book(Long eventId, String email, int quantity) {
        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (quantity < 1 || quantity > properties.maxPlacesParReservation()) {
            throw new InvalidReservationException(
                    "Le nombre de places doit être compris entre 1 et "
                            + properties.maxPlacesParReservation());
        }
        if (quantity > event.availablePlaces()) {
            throw new NotEnoughPlacesException();
        }

        BigDecimal unitPrice = pricingService.calculateUnitPrice(
                event.getBasePrice(), event.getDate().toLocalDate());
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
        event.reservePlaces(quantity);
        return reservationRepository.save(new Reservation(event, email, quantity, totalPrice));
    }

    @Transactional
    public void cancel(Long eventId, Long reservationId) {
        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Reservation reservation = reservationRepository.findById(reservationId)
                .filter(found -> found.getEvent().getId().equals(eventId))
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        event.releasePlaces(reservation.getQuantity());
        reservationRepository.delete(reservation);
    }
}
