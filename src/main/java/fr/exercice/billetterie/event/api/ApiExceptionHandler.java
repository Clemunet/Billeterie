package fr.exercice.billetterie.event.api;

import fr.exercice.billetterie.event.EventNotFoundException;
import fr.exercice.billetterie.reservation.InvalidReservationException;
import fr.exercice.billetterie.reservation.NotEnoughPlacesException;
import fr.exercice.billetterie.reservation.ReservationNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({EventNotFoundException.class, ReservationNotFoundException.class})
    public ResponseEntity<ApiError> notFound(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<ApiError> invalidReservation(InvalidReservationException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiError(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler({NotEnoughPlacesException.class,
            ObjectOptimisticLockingFailureException.class,
            PessimisticLockingFailureException.class})
    public ResponseEntity<ApiError> reservationConflict(RuntimeException exception) {
        String message = exception instanceof NotEnoughPlacesException
                ? exception.getMessage()
                : "Conflit avec une autre réservation, veuillez réessayer";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(message, Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validationFailed(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fields.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ApiError("Données invalides", fields));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> invalidJson() {
        return ResponseEntity.badRequest()
                .body(new ApiError("Corps JSON invalide", Map.of()));
    }
}
