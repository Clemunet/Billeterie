package fr.exercice.billetterie.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event create(String name, LocalDateTime date, int capacity, BigDecimal basePrice) {
        return repository.save(new Event(null, name, date, capacity, basePrice));
    }

    public List<Event> listUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return repository.findAll().stream()
                .filter(event -> event.date().isAfter(now))
                .sorted(Comparator.comparing(Event::date))
                .toList();
    }

    public Event getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }
}
