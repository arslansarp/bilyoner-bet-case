package com.bilyoner.betting.request;

import com.bilyoner.betting.entity.BetType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetSlipRequest {

    @JsonProperty("event_id")
    private String eventId;

    @Enumerated(EnumType.STRING)
    @JsonProperty("bet_type")
    private BetType betType;

    @JsonProperty("bet_rate")
    private double betRate;

    @JsonProperty("bet_amount")
    private int betAmount;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("customer_id")
    private String customerId;
}
