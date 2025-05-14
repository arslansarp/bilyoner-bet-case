package com.bilyoner.betting.service.impl;

import com.bilyoner.betting.constants.EventConstants;
import com.bilyoner.betting.dto.EventDto;
import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.mappers.EventMapper;
import com.bilyoner.betting.repository.EventRepository;
import com.bilyoner.betting.service.BulletinBroadcastService;
import com.bilyoner.betting.service.EventService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final static double MIN_RATE_VALUE = EventConstants.MIN_RATE_VALUE;
    private final static double MAX_RATE_VALUE = EventConstants.MAX_RATE_VALUE;

    private final BulletinBroadcastService bulletinBroadcastService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        Event event = eventRepository.save(eventMapper.toEntity(eventDto));
        return eventMapper.toDto(event);
    }

    @Override
    public List<EventDto> getEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(eventMapper::toDto).toList();
    }

    @Override
    @Transactional
    @Scheduled(fixedRateString = "${bet.slip.update-event-second}", timeUnit = TimeUnit.SECONDS)
    public void updateAllEventOdds() {
        List<Event> events = eventRepository.findAll();
        events.forEach( event -> {
            event.setHomeWinRate(generateRandomBetRate());
            event.setDrawRate(generateRandomBetRate());
            event.setAwayWinRate(generateRandomBetRate());
            });
        eventRepository.saveAll(events);
        bulletinBroadcastService.sendUpdatedBulletin(events);
    }

    private double generateRandomBetRate() {
        Random random = new Random();
        double rate = MIN_RATE_VALUE + (MAX_RATE_VALUE - MIN_RATE_VALUE) * random.nextDouble();
        return Math.round(rate * 100.0) / 100.0;
    }
}
