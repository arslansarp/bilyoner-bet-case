package com.bilyoner.betting.service;

import com.bilyoner.betting.dto.BetSlipDto;
import com.bilyoner.betting.entity.BetSlip;

import java.util.List;

public interface BetSlipService {

    void createBetSlip(List<BetSlipDto> betSlipDtos, String customerId);
}
