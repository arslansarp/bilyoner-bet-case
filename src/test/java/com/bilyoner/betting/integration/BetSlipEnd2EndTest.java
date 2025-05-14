package com.bilyoner.betting.integration;

import com.bilyoner.betting.entity.BetSlip;
import com.bilyoner.betting.entity.BetType;
import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.repository.BetSlipRepository;
import com.bilyoner.betting.repository.EventRepository;
import com.bilyoner.betting.request.BetSlipRequest;
import com.bilyoner.betting.service.BetSlipService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BetSlipEnd2EndTest {

    private Event event;
    private Event event2;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private BetSlipService betSlipService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BetSlipRepository betSlipRepository;

    @BeforeEach
    public void setUp() {
        betSlipRepository.deleteAll();
        eventRepository.deleteAll();
        event = eventRepository.save(new Event(null,"league","home","away",1.30,2,3, LocalDateTime.now()));
        event2 = eventRepository.save(new Event(null,"league2","home2","away2",1,2,2.50, LocalDateTime.now()));
    }

    @Test
    void testCreateBetSlip() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Customer-Id", "082068af-e02e-4736-982b-536166d6edad");

        String betSlips = String.format("[" +
                        "{" +
                        "\"event_id\": \"%s\", " +
                        "\"bet_rate\": \"1.30\", " +
                        "\"bet_type\": \"HOME_WIN\", " +
                        "\"bet_amount\": 1.15, " +
                        "\"quantity\": 2.0" +
                        "}," +
                        "{" +
                        "\"event_id\": \"%s\", " +
                        "\"bet_rate\": \"2.50\", " +
                        "\"bet_type\": \"AWAY_WIN\", " +
                        "\"bet_amount\": 2.00, " +
                        "\"quantity\": 1.0" +
                        "}" +
                        "]",
                event.getId(), event2.getId()
        );


        String url = "http://localhost:"+port+"/bulletin/betslip";
        HttpEntity<String> request = new HttpEntity<>(betSlips, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                url,
                request,
                Void.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateBetSlipOddsChanged() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Customer-Id", "082068af-e02e-4736-982b-536166d6edad");

        String betSlip = String.format("[" +
                        "{" +
                        "\"event_id\": \"%s\", " +
                        "\"bet_rate\": \"4.0\", " +
                        "\"bet_type\": \"HOME_WIN\", " +
                        "\"bet_amount\": 200, " +
                        "\"quantity\": 2.0" +
                        "},",
                event.getId()
        );

        String url = "http://localhost:"+port+"/bulletin/betslip";
        HttpEntity<String> request = new HttpEntity<>(betSlip, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                url,
                request,
                Void.class
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateBetSlipExceedTheMaximumCouponCount() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Customer-Id", "082068af-e02e-4736-982b-536166d6edad");
        BetSlipRequest betSlipRequest = BetSlipRequest.builder().eventId(event.getId().toString())
                .betAmount(200).betType(BetType.HOME_WIN).betRate(event.getHomeWinRate()).quantity(300)
                .customerId("082068af-e02e-4736-982b-536166d6edad").build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(List.of(betSlipRequest));
        String url = "http://localhost:"+port+"/bulletin/betslip";
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        BetSlip betSlip1 = new BetSlip();
        betSlip1.setQuantity(300);
        betSlip1.setBetAmount(200);
        betSlip1.setBetRate(event.getHomeWinRate());
        betSlip1.setEvent(event);
        betSlip1.setBetType(BetType.HOME_WIN);
        betSlip1.setCustomerId("082068af-e02e-4736-982b-536166d6edad");
        betSlipRepository.save(betSlip1);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                url,
                request,
                Void.class
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
