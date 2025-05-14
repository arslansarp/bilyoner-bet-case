package com.bilyoner.betting.integration;

import com.bilyoner.betting.dto.EventDto;
import com.bilyoner.betting.repository.EventRepository;
import com.bilyoner.betting.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllEvents() throws Exception {
        String dateTimeString = "2025-05-14T10:15:30";
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString);
        EventDto event1 = new EventDto("Test League", "Home", "Away", 1.15, 2, 3, localDateTime);
        EventDto event2 = new EventDto("Test League2", "Home", "Away", 1.20, 1.5, 3, localDateTime);
        eventService.createEvent(event1);
        eventService.createEvent(event2);

        String url = "http://localhost:"+port+"/bulletin/events";
        ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EventDto>>() {}
        );
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void shouldCreateNewEvent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String eventJson = "{"
                + "\"league\": \"Test League\", "
                + "\"home_team\": \"Home\", "
                + "\"away_team\": \"Away\", "
                + "\"home_win_rate\": 1.15, "
                + "\"draw_rate\": 2.0, "
                + "\"away_win_rate\": 3.0, "
                + "\"start_time\": \"2025-05-14T10:00:00\""
                + "}";
        String url = "http://localhost:"+port+"/bulletin/event";
        HttpEntity<String> request = new HttpEntity<>(eventJson, headers);

        ResponseEntity<EventDto> response = restTemplate.postForEntity(
                url,
                request,
                EventDto.class
        );
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCode().value());
        assertEquals("Test League", response.getBody().getLeague());
        assertEquals("Home", response.getBody().getHomeTeam());
        assertEquals("Away", response.getBody().getAwayTeam());
        assertEquals(1.15, response.getBody().getHomeWinRate());
        assertEquals(2.0, response.getBody().getDrawRate());
        assertEquals(3.0, response.getBody().getAwayWinRate());
    }
}

