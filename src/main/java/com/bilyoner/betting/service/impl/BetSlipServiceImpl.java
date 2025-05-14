package com.bilyoner.betting.service.impl;

import com.bilyoner.betting.config.BetSlipConfig;
import com.bilyoner.betting.dto.BetSlipDto;
import com.bilyoner.betting.entity.BetSlip;
import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.exceptions.ErrorMessage;
import com.bilyoner.betting.exceptions.ErrorResponse;
import com.bilyoner.betting.exceptions.EventNotFoundException;
import com.bilyoner.betting.exceptions.EventOddsChangedException;
import com.bilyoner.betting.exceptions.MaximumLimitOfCouponException;
import com.bilyoner.betting.exceptions.PaymentExceedLimitForOneCouponException;
import com.bilyoner.betting.mappers.BetSlipMapper;
import com.bilyoner.betting.repository.BetSlipRepository;
import com.bilyoner.betting.repository.EventRepository;
import com.bilyoner.betting.service.BetSlipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BetSlipServiceImpl implements BetSlipService {

    private final TransactionTemplate transactionTemplate;
    private final BetSlipMapper betSlipMapper;
    private final BetSlipRepository betSlipRepository;
    private final EventRepository eventRepository;
    private final BetSlipConfig betSlipConfig;

    @Override
    public void createBetSlip(List<BetSlipDto> betSlipDtos, String customerId) {
        transactionTemplate.setTimeout(betSlipConfig.getTimeoutSeconds());
        transactionTemplate.executeWithoutResult(status -> {
        checkTotalMoneyExceedsLimit(betSlipDtos);
        Map<String, UUID> eventIdMap = betSlipDtos.stream()
                .collect(Collectors.toMap(
                        BetSlipDto::getEventId,
                        dto -> UUID.fromString(dto.getEventId())
                ));

        List<UUID> eventIds = eventIdMap.values().stream().toList();
        Map<UUID, Event> eventMap = getEventsMap(eventIds);
        checkOddsAreSync(betSlipDtos, eventMap, eventIdMap);
        checkMaximumCouponCount(customerId, betSlipDtos, eventIds);

        List<BetSlip> betSlips = betSlipDtos.stream().map(dto -> betSlipMapper.toEntity(dto, customerId)).toList();
        betSlipRepository.saveAll(betSlips); });
    }

    private Map<UUID, Event> getEventsMap(List<UUID> uuids) {
        List<Event> events = eventRepository.findAllById(uuids);
        if(events.isEmpty())
            throw new EventNotFoundException(ErrorMessage.EVENT_NOT_FOUND.getMessage());
        return events.stream()
                .collect(Collectors.toMap(Event::getId, Function.identity()));
    }

    private void checkOddsAreSync(List<BetSlipDto> betSlipDtos, Map<UUID, Event> eventMap, Map<String, UUID> eventIdMap) {
        betSlipDtos.forEach(betSlipDto -> {
            double eventBetRate = eventMap.get(eventIdMap.get(betSlipDto.getEventId()))
                    .getRateByType(betSlipDto.getBetType());
            if(eventBetRate != betSlipDto.getBetRate()){
                throw new EventOddsChangedException(ErrorMessage.EVENT_ODDS_CHANGED.getMessage());
            }
        });
    }

    private void checkMaximumCouponCount(String customerId, List<BetSlipDto> betSlipDtos, List<UUID> eventIds) {
        Map<String, Integer> sumMap = getSumCouponCountByEventIdsAndCustomer(customerId, eventIds);
        if(!sumMap.isEmpty()) {
            betSlipDtos.forEach(betSlipDto -> {
                if ( betSlipDto.getQuantity() + sumMap.get(betSlipDto.getEventId()) > betSlipConfig.getMaxCount() )
                    throw new MaximumLimitOfCouponException(ErrorMessage.BET_COUNT_EXCEEDED.getMessage());
            });
        }
    }

    private Map<String, Integer> getSumCouponCountByEventIdsAndCustomer(String customerId, List<UUID> eventIds) {
        List<Object[]> results = betSlipRepository.sumCouponCountByEventIdsAndCustomerId(eventIds, customerId);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> ((Long) row[1]).intValue()
                ));
    }

    private void checkTotalMoneyExceedsLimit(List<BetSlipDto> betSlipDtos) {
        betSlipDtos.forEach(betSlipDto -> {
            if ( betSlipDto.getBetAmount() * betSlipDto.getQuantity() > betSlipConfig.getMaximumLimitForOneCoupon() )
                throw new PaymentExceedLimitForOneCouponException(ErrorMessage.PAYMENT_EXCEEDS_LIMIT.getMessage());
        });
    }
}
