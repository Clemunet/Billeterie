package fr.exercice.billetterie.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Event(
        Long id,
        String name,
        LocalDateTime date,
        int capacity,
        BigDecimal basePrice
) {

    public int availablePlaces() {
        return capacity;
    }
}
