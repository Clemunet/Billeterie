package fr.exercice.billetterie.event.api;

import fr.exercice.billetterie.event.Event;
import fr.exercice.billetterie.event.EventService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody CreateEventRequest request) {
        Event event = service.create(
                request.name(), request.date(), request.capacity(), request.basePrice());
        return ResponseEntity.created(URI.create("/api/events/" + event.id()))
                .body(EventResponse.from(event));
    }

    @GetMapping
    public List<EventResponse> listUpcoming() {
        return service.listUpcoming().stream().map(EventResponse::from).toList();
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id) {
        return EventResponse.from(service.getById(id));
    }
}
