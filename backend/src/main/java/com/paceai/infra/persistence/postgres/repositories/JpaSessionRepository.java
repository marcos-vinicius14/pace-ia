package com.paceai.infra.persistence.postgres.repositories;

import com.paceai.infra.persistence.postgres.entity.SessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaSessionRepository extends JpaRepository<SessionEntity, UUID> {

    List<SessionEntity> findByPlanId(UUID planId);

    List<SessionEntity> findByPlanIdOrderByScheduledDateAsc(UUID planId);

    List<SessionEntity> findByPlanIdAndScheduledDateBetween(
            UUID planId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<SessionEntity> findByPlanIdInOrderByScheduledDateAsc(List<UUID> planIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM SessionEntity s WHERE s.planId = :planId")
    void deleteByPlanId(@Param("planId") UUID planId);
}
