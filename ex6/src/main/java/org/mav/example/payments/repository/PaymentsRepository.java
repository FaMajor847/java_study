package org.mav.example.payments.repository;

import org.mav.example.payments.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUserId(Long userId);

    Optional<Payment> findByExternalId(String externalId);
}
