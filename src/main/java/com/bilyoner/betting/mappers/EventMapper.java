package com.bilyoner.betting.mappers;

import com.bilyoner.betting.dto.EventDto;
import com.bilyoner.betting.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEntity(EventDto dto);
    EventDto toDto(Event event);
}
