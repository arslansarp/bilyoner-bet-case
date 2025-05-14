package com.bilyoner.betting.mappers;

import com.bilyoner.betting.dto.BetSlipDto;
import com.bilyoner.betting.entity.BetSlip;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface BetSlipMapper {

    @Mapping(target = "event.id", source = "dto.eventId")
    BetSlip toEntity(BetSlipDto dto, @Context String customerId);

    @Mapping(target = "eventId", source = "event.id")
    BetSlipDto toDto(BetSlip betSlip);

    @AfterMapping
    default void setCustomerId(@MappingTarget BetSlip betSlip, @Context String customerId) {
        betSlip.setCustomerId(customerId);
    }

}
