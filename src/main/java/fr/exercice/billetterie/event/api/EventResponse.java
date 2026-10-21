package fr.exercice.billetterie.event.api;

import fr.exercice.billetterie.event.Event;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        LocalDateTime date,
        int capacity,
        BigDecimal basePrice,
        int availablePlaces
) {

    public static EventResponse from(Event event) {
        return new EventResponse(
                event.id(), event.name(), event.date(), event.capacity(),
                event.basePrice(), event.availablePlaces());
    }
}
