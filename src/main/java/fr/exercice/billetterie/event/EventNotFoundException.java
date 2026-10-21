package fr.exercice.billetterie.event;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Long id) {
        super("Événement introuvable : " + id);
    }
}
