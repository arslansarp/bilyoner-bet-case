package com.bilyoner.betting.controller;

import com.bilyoner.betting.dto.EventDto;
import com.bilyoner.betting.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("bulletin")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getAllEvents() {
         List<EventDto> entities = eventService.getEvents();
        return ResponseEntity.status(HttpStatus.OK).body(entities);
    }

    @PostMapping("/event")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto event) {
        EventDto eventDto = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDto);
    }
}
