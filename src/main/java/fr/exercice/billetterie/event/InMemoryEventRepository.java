package fr.exercice.billetterie.event;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEventRepository implements EventRepository {

    private final ConcurrentHashMap<Long, Event> events = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public Event save(Event event) {
        Long id = event.id() == null ? nextId.getAndIncrement() : event.id();
        Event saved = new Event(id, event.name(), event.date(), event.capacity(), event.basePrice());
        events.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public List<Event> findAll() {
        return List.copyOf(events.values());
    }
}
