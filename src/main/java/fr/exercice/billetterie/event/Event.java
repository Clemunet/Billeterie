package fr.exercice.billetterie.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "reserved_places", nullable = false)
    private int reservedPlaces;

    @Version
    private Long version;

    protected Event() {
    }

    public Event(String name, LocalDateTime date, int capacity, BigDecimal basePrice) {
        this.name = name;
        this.date = date;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public int availablePlaces() {
        return capacity - reservedPlaces;
    }

    public void reservePlaces(int quantity) {
        reservedPlaces += quantity;
    }

    public void releasePlaces(int quantity) {
        reservedPlaces -= quantity;
    }
}
