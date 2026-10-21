package fr.exercice.billetterie.reservation;

public class NotEnoughPlacesException extends RuntimeException {

    public NotEnoughPlacesException() {
        super("Plus assez de places disponibles");
    }
}
