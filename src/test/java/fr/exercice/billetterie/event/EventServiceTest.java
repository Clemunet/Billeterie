package fr.exercice.billetterie.event;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class EventServiceTest {

    @Test
    void unknownEventRaisesBusinessException() {
        EventRepository repository = mock(EventRepository.class);
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> new EventService(repository).getById(42L));
    }
}
