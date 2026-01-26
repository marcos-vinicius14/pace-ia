package com.paceai.infra.persistence.postgres.repositories;

import com.paceai.infra.persistence.postgres.entity.StravaActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStravaActivityRepository extends JpaRepository<StravaActivityEntity, Long> {
}
