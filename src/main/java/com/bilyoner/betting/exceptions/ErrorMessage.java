package com.bilyoner.betting.exceptions;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    EVENT_ODDS_CHANGED("Event odds changed"),
    BET_COUNT_EXCEEDED("Bet count exceeded"),
    BET_SLIP_NOT_FOUND("Bet slip not found"),
    EVENT_NOT_FOUND("Event not found"),
    PAYMENT_EXCEEDS_LIMIT("Payment exceeds limit for one coupon");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
