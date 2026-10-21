package fr.exercice.billetterie.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event create(String name, LocalDateTime date, int capacity, BigDecimal basePrice) {
        return repository.save(new Event(name, date, capacity, basePrice));
    }

    public List<Event> listUpcoming() {
        return repository.findByDateAfterOrderByDateAsc(LocalDateTime.now());
    }

    public Event getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }
}
