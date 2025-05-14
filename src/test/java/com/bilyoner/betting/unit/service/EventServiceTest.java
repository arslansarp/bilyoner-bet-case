package com.bilyoner.betting.unit.service;

import com.bilyoner.betting.dto.EventDto;
import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.mappers.EventMapper;
import com.bilyoner.betting.repository.EventRepository;
import com.bilyoner.betting.service.BulletinBroadcastService;
import com.bilyoner.betting.service.impl.EventServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    private List<EventDto> eventDtos;
    private List<Event> events;

    @Mock
    private BulletinBroadcastService bulletinBroadcastService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        Event event = createEvent("test_league", "test_home_team", "test_away");
        Event event2 = createEvent("test_league2", "test_home_team2", "test_away2");
        this.events =  List.of(event, event2);

        EventDto eventDto = createEventDto("test_league", "test_home_team", "test_away");
        EventDto eventDto2 = createEventDto("test_league2", "test_home_team2", "test_away2");
        this.eventDtos = Arrays.asList(eventDto, eventDto2);
    }

    @Test
    void createEvent_ShouldMapAndSave() {
        Event event = events.get(0);
        EventDto eventDto = eventDtos.get(0);

        when(eventMapper.toEntity(eventDto)).thenReturn(event);
        when(eventRepository.save(any())).thenReturn(event);

        eventService.createEvent(eventDto);

        verify(eventMapper).toEntity(eventDto);
        event = eventRepository.save(event);

        assertEquals(eventDto.getLeague(), event.getLeague());
        assertEquals(eventDto.getHomeTeam(), event.getHomeTeam());
        assertEquals(eventDto.getAwayTeam(), event.getAwayTeam());

    }

    @Test
    void getEvents_ShouldReturnMappedDtos() {
        Event event = events.get(0);
        Event event2 = events.get(1);
        EventDto eventDto = eventDtos.get(0);
        EventDto eventDto2 = eventDtos.get(1);

        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDto(event)).thenReturn(eventDto);
        when(eventMapper.toDto(event2)).thenReturn(eventDto2);

        List<EventDto> result = eventService.getEvents();

        verify(eventRepository).findAll();
        assertThat(result).containsExactly(eventDto, eventDto2);
    }

    @Test
    void updateAllEventOdds_ShouldUpdateRatesAndSendBulletin() {

        when(eventRepository.findAll()).thenReturn(events);

        eventService.updateAllEventOdds();

        verify(eventRepository).findAll();
        verify(eventRepository).saveAll(events);
        verify(bulletinBroadcastService).sendUpdatedBulletin(events);


        for (Event e : events) {
            assertThat(e.getHomeWinRate()).isBetween(1.01, 10.0);
            assertThat(e.getDrawRate()).isBetween(1.01, 10.0);
            assertThat(e.getAwayWinRate()).isBetween(1.01, 10.0);
        }
    }

    private Event createEvent(String league, String homeTeam, String awayTeam) {
        Event event = new Event();
        event.setLeague(league);
        event.setHomeTeam(homeTeam);
        event.setAwayTeam(awayTeam);
        event.setHomeWinRate(2.0);
        event.setAwayWinRate(4.0);
        event.setDrawRate(5.0);
        event.setStartTime(LocalDateTime.now().plusHours(2));
        return event;
    }

    private EventDto createEventDto(String league, String homeTeam, String awayTeam) {
        EventDto eventDto = new EventDto();
        eventDto.setLeague(league);
        eventDto.setHomeTeam(homeTeam);
        eventDto.setAwayTeam(awayTeam);
        eventDto.setHomeWinRate(2.0);
        eventDto.setAwayWinRate(4.0);
        eventDto.setDrawRate(5.0);
        eventDto.setStartTime(LocalDateTime.now().plusHours(2));
        return eventDto;
    }
}
