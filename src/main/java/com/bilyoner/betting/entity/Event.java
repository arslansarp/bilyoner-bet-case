package com.bilyoner.betting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String league;

    @Column(name = "home_team", nullable = false)
    private String homeTeam;

    @Column(name = "away_team", nullable = false)
    private String awayTeam;

    @Column(name = "home_win_rate", nullable = false)
    private double homeWinRate;

    @Column(name = "draw_rate", nullable = false)
    private double drawRate;

    @Column(name = "away_win_rate", nullable = false)
    private double awayWinRate;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    public double getRateByType(BetType type) {
        switch (type) {
            case HOME_WIN -> {
                return this.homeWinRate;
            }
            case AWAY_WIN -> {
                return this.awayWinRate;
            }
            case DRAW -> {
                return this.drawRate;
            }
            default -> {
                throw new RuntimeException("Invalid bet type");
            }
        }
    }
}
