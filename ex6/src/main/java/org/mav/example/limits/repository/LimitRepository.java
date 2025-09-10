package org.mav.example.limits.repository;

import org.mav.example.limits.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByClientIdAndDay(Long clientId, LocalDate day);

    @Query("select distinct l.clientId from Limit l")
    List<Long> findDistinctClientIds();

}
