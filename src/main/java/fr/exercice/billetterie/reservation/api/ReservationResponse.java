package fr.exercice.billetterie.reservation.api;

import fr.exercice.billetterie.reservation.Reservation;
import java.math.BigDecimal;

public record ReservationResponse(
        Long id,
        Long eventId,
        String email,
        int quantity,
        BigDecimal totalPrice
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(), reservation.getEvent().getId(),
                reservation.getEmail(), reservation.getQuantity(), reservation.getTotalPrice());
    }
}
