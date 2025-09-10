package org.mav.example.payments.service;

import jakarta.transaction.Transactional;
import org.mav.example.payments.api.dto.PaymentRequest;
import org.mav.example.payments.client.ProductsClient;
import org.mav.example.payments.domain.Payment;
import org.mav.example.payments.domain.PaymentStatus;
import org.mav.example.payments.repository.PaymentsRepository;
import org.mav.example.products.api.dto.ProductDto;
import org.mav.example.products.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final ProductsClient productsClient;

    public PaymentService(PaymentsRepository paymentsRepository, ProductsClient productsClient) {
        this.paymentsRepository = paymentsRepository;
        this.productsClient = productsClient;
    }

    @Transactional
    public Payment execute(PaymentRequest request) {
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be > 0");
        }

        ProductDto product = productsClient.findById(request.productId());

        if (product.balance().compareTo(request.amount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        Payment payment = new Payment();
        payment.setUserId(request.userId());
        payment.setProductId(request.productId());
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentsRepository.save(payment);
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
