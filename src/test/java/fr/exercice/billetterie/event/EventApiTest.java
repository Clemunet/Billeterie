package fr.exercice.billetterie.event;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.exercice.billetterie.event.api.CreateEventRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EventApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validEventReturns201WithLocationAndAvailablePlaces() throws Exception {
        CreateEventRequest request = new CreateEventRequest(
                "Concert", LocalDateTime.now().plusDays(10), 50, new BigDecimal("25.00"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("/api/events/\\d+")))
                .andExpect(jsonPath("$.name").value("Concert"))
                .andExpect(jsonPath("$.availablePlaces").value(50));
    }

    @Test
    void invalidEventReturns400WithFieldNames() throws Exception {
        CreateEventRequest request = new CreateEventRequest(
                " ", LocalDateTime.now().minusDays(1), 0, BigDecimal.ZERO);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.date").exists())
                .andExpect(jsonPath("$.fieldErrors.capacity").exists())
                .andExpect(jsonPath("$.fieldErrors.basePrice").exists());
    }

    @Test
    void unknownEventReturns404WithoutStacktrace() throws Exception {
        mockMvc.perform(get("/api/events/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Événement introuvable : 999999"))
                .andExpect(jsonPath("$.fieldErrors").isMap());
    }
}
