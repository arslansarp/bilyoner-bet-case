package com.bilyoner.betting.controller;

import com.bilyoner.betting.dto.BetSlipDto;
import com.bilyoner.betting.service.BetSlipService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BetSlipController {

    private final BetSlipService betSlipService;

    @PostMapping("bulletin/betslip")
    public ResponseEntity<Void> createBetSlip(@Valid @RequestBody List<BetSlipDto> dtos,
                                              @RequestHeader("X-Customer-Id") String customerId) {
        betSlipService.createBetSlip(dtos, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
