package com.bilyoner.betting.exceptions;

public class PaymentExceedLimitForOneCouponException extends RuntimeException {
    public PaymentExceedLimitForOneCouponException(String message) {
        super(message);
    }
}
