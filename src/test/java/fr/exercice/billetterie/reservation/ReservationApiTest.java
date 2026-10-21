package fr.exercice.billetterie.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void bookingConflictCancellationAndNewBooking() throws Exception {
        long eventId = createEvent(10);
        String url = "/api/events/" + eventId + "/reservations";

        String booking = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody("client@example.com", 6)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern(url + "/\\d+")))
                .andExpect(jsonPath("$.eventId").value(eventId))
                .andExpect(jsonPath("$.quantity").value(6))
                .andExpect(jsonPath("$.totalPrice").value(120.00))
                .andReturn().getResponse().getContentAsString();
        long reservationId = objectMapper.readTree(booking).get("id").asLong();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody("autre@example.com", 5)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());

        mockMvc.perform(delete(url + "/" + reservationId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/events/" + eventId))
                .andExpect(jsonPath("$.availablePlaces").value(10));
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody("autre@example.com", 6)))
                .andExpect(status().isCreated());
    }

    @Test
    void invalidReservationReturns400WithFields() throws Exception {
        long eventId = createEvent(10);
        String url = "/api/events/" + eventId + "/reservations";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody("bad-email", 0)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.quantity").exists());
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody("client@example.com", 7)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownEventAndReservationReturn404() throws Exception {
        mockMvc.perform(post("/api/events/999999/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody("client@example.com", 1)))
                .andExpect(status().isNotFound());

        long eventId = createEvent(10);
        mockMvc.perform(delete("/api/events/" + eventId + "/reservations/999999"))
                .andExpect(status().isNotFound());
    }

    private long createEvent(int capacity) throws Exception {
        String json = objectMapper.writeValueAsString(Map.of(
                "name", "Concert réservation",
                "date", java.time.LocalDateTime.now().plusDays(10),
                "capacity", capacity,
                "basePrice", 20.00));
        String response = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode body = objectMapper.readTree(response);
        return body.get("id").asLong();
    }

    private String reservationBody(String email, int quantity) throws Exception {
        return objectMapper.writeValueAsString(Map.of("email", email, "quantity", quantity));
    }
}
