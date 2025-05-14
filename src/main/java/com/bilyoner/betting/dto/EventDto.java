package com.bilyoner.betting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @JsonProperty("league")
    private String league;

    @JsonProperty("home_team")
    private String homeTeam;

    @JsonProperty("away_team")
    private String awayTeam;

    @JsonProperty("home_win_rate")
    private double homeWinRate;

    @JsonProperty("draw_rate")
    private double drawRate;

    @JsonProperty("away_win_rate")
    private double awayWinRate;

    @JsonProperty("start_time")
    private LocalDateTime startTime;
}
