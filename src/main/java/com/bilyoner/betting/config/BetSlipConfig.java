package com.bilyoner.betting.config;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "bet.slip")
@Validated
@Data
public class BetSlipConfig {

    @Min(1)
    private int maxCount;

    @Min(1)
    private int timeoutSeconds;

    @Min(1)
    private int maximumLimitForOneCoupon;

}
