package com.bilyoner.betting.service;

import java.util.List;
import com.bilyoner.betting.dto.EventDto;

public interface EventService {
    EventDto createEvent(EventDto event);

    List<EventDto> getEvents();

    void updateAllEventOdds();
}
