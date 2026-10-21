package fr.exercice.billetterie.event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(Long id);

    List<Event> findAll();
}
