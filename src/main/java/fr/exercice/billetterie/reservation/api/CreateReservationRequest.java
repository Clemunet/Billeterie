package fr.exercice.billetterie.reservation.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReservationRequest(
        @NotBlank @Email String email,
        @NotNull @Min(1) Integer quantity
) {
}
