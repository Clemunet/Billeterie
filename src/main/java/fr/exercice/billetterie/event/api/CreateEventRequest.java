package fr.exercice.billetterie.event.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateEventRequest(
        @NotBlank String name,
        @NotNull @Future LocalDateTime date,
        @NotNull @Min(1) Integer capacity,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal basePrice
) {
}
