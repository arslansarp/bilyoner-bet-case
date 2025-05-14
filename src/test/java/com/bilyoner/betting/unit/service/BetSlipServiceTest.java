package com.bilyoner.betting.unit.service;

import com.bilyoner.betting.entity.BetType;
import com.bilyoner.betting.service.impl.BetSlipServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.bilyoner.betting.dto.BetSlipDto;
import com.bilyoner.betting.entity.BetSlip;
import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.mappers.BetSlipMapper;
import com.bilyoner.betting.repository.BetSlipRepository;
import com.bilyoner.betting.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetSlipServiceImplTest {

    @Mock
    private BetSlipMapper betSlipMapper;

    @Mock
    private BetSlipRepository betSlipRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private BetSlipServiceImpl betSlipService;

    private BetSlipDto betSlipDto;
    private Event event;

    @BeforeEach
    void setUp() {
        betSlipDto = new BetSlipDto();
        betSlipDto.setEventId(UUID.randomUUID().toString());
        betSlipDto.setBetRate(2.0);
        betSlipDto.setBetType(BetType.HOME_WIN);
        betSlipDto.setBetAmount(100.0);
        betSlipDto.setQuantity(2);

        event = new Event();
        event.setId(UUID.fromString(betSlipDto.getEventId()));
        event.setHomeWinRate(2.0);
        event.setDrawRate(3.0);
        event.setAwayWinRate(4.0);
    }

    @Test
    void createBetSlip_shouldSaveValidBetSlips() {
        String customerId = UUID.randomUUID().toString();
        List<BetSlipDto> betSlipDtos = List.of(betSlipDto);
        List<Event> events = List.of(event);
        BetSlip betSlip = new BetSlip();

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{betSlipDto.getEventId(), 0});

        when(eventRepository.findAllById(any())).thenReturn(events);
        when(betSlipMapper.toEntity(any(), eq(customerId))).thenReturn(betSlip);
        when(betSlipRepository.sumCouponCountByEventIdsAndCustomerId(any(), any()))
                .thenReturn(results);

        betSlipService.createBetSlip(betSlipDtos, customerId);

        verify(betSlipRepository).saveAll(anyList());
    }

    @Test
    void createBetSlip_shouldThrowWhenEventRateNotSynced() {
        String customerId = UUID.randomUUID().toString();
        Event updatedEvent = new Event();
        updatedEvent.setHomeWinRate(event.getHomeWinRate() + 1.0);
        updatedEvent.setId(event.getId());

        when(eventRepository.findAllById(any())).thenReturn(List.of(updatedEvent));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            betSlipService.createBetSlip(List.of(betSlipDto), customerId);
        });

        assertEquals("Event bet rate is not correct", ex.getMessage());
    }

    @Test
    void createBetSlip_shouldThrowWhenMaximumCouponExceeded() {
        String customerId = UUID.randomUUID().toString();

        when(eventRepository.findAllById(anyList())).thenReturn(List.of(event));

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{betSlipDto.getEventId(), 499});

        when(betSlipRepository.sumCouponCountByEventIdsAndCustomerId(anyList(), any()))
                .thenReturn(results);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            betSlipService.createBetSlip(List.of(betSlipDto), customerId);
        });

        assertEquals("maximum", ex.getMessage());
    }

    @Test
    void createBetSlip_shouldThrowWhenNoEventsFound() {
        String customerId = UUID.randomUUID().toString();

        when(eventRepository.findAllById(any())).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            betSlipService.createBetSlip(List.of(betSlipDto), customerId);
        });

        assertEquals("No events found", ex.getMessage());
    }
}

