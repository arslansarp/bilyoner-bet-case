package com.bilyoner.betting.repository;

import com.bilyoner.betting.entity.BetSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BetSlipRepository extends JpaRepository<BetSlip, UUID> {

    @Query("SELECT SUM(b.quantity) FROM BetSlip b WHERE b.event.id = :eventId")
    int sumCouponCountByEventId(@Param("eventId") UUID eventId);

    @Query("SELECT b.event.id, SUM(b.quantity) " +
            "FROM BetSlip b " +
            "WHERE b.event.id IN :eventIds AND b.customerId = :customerId " +
            "GROUP BY b.event.id")
    List<Object[]> sumCouponCountByEventIdsAndCustomerId(@Param("eventIds") List<UUID> eventIds,
                                                         @Param("customerId") String customerId);

}
