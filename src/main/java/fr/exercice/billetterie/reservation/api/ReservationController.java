package fr.exercice.billetterie.reservation.api;

import fr.exercice.billetterie.reservation.Reservation;
import fr.exercice.billetterie.reservation.ReservationService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events/{eventId}/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> book(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateReservationRequest request
    ) {
        Reservation reservation = service.book(eventId, request.email(), request.quantity());
        return ResponseEntity.created(URI.create(
                        "/api/events/" + eventId + "/reservations/" + reservation.getId()))
                .body(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long eventId,
            @PathVariable Long reservationId
    ) {
        service.cancel(eventId, reservationId);
        return ResponseEntity.noContent().build();
    }
}
