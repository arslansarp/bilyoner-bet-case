package com.bilyoner.betting.dto;

import com.bilyoner.betting.entity.BetType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class BetSlipDto {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("bet_rate")
    private double betRate;

    @Enumerated(EnumType.STRING)
    @JsonProperty("bet_type")
    private BetType betType;

    @JsonProperty("bet_amount")
    private double betAmount;

    @Column(name = "quantity")
    private int quantity;
}
