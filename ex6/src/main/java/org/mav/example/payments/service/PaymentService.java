package org.mav.example.payments.service;

import jakarta.transaction.Transactional;
import org.flywaydb.core.internal.util.StringUtils;
import org.mav.example.limits.service.LimitService;
import org.mav.example.payments.api.dto.PaymentRequest;
import org.mav.example.payments.client.ProductsClient;
import org.mav.example.payments.domain.Payment;
import org.mav.example.payments.domain.PaymentStatus;
import org.mav.example.payments.repository.PaymentsRepository;
import org.mav.example.products.api.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final ProductsClient productsClient;
    private final LimitService limits;

    public PaymentService(PaymentsRepository paymentsRepository, ProductsClient productsClient, LimitService limits) {
        this.paymentsRepository = paymentsRepository;
        this.productsClient = productsClient;
        this.limits = limits;
    }

    @Transactional
    public Payment execute(PaymentRequest request) {
        if (!StringUtils.hasText(request.externalId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ExternalId is required");
        }
        var existing = paymentsRepository.findByExternalId(request.externalId());
        if (existing.isPresent()) {
            return existing.get();
        }
        if (request.userId() == null || request.userId() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must be positive");
        }

        boolean reserved = false;
        Payment p = null;

        try {
            limits.tryReserve(request.userId(), request.amount());
            reserved = true;
            ProductDto product = productsClient.findById(request.productId());

            if (product.balance().compareTo(request.amount()) < 0) {
                // важно: вернуть лимит перед тем как бросить 400
                limits.restore(request.userId(), request.amount());
                reserved = false;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }

            p = new Payment();
            p.setUserId(request.userId());
            p.setProductId(request.productId());
            p.setAmount(request.amount());
            p.setExternalId(request.externalId());
            p.setStatus(PaymentStatus.PENDING);
            p = paymentsRepository.save(p);
            p.setStatus(PaymentStatus.SUCCESS);
            return paymentsRepository.save(p);
        } catch (RuntimeException ex) {
            if (reserved) {
                limits.restore(request.userId(), request.amount());
            }
            if (p != null && p.getId() != null && p.getStatus() != PaymentStatus.FAILED) {
                p.setStatus(PaymentStatus.FAILED);
                paymentsRepository.save(p);
            }
            throw ex;
        }
    }

    public Optional<Payment> findById(Long id) {
        if (id == null || id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id must be positive");
        }
        return paymentsRepository.findById(id);
    }

    public List<Payment> findAllByUserId(Long userId) {
        if (userId == null || userId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must be positive");
        }
        return paymentsRepository.findAllByUserId(userId);
    }


}
