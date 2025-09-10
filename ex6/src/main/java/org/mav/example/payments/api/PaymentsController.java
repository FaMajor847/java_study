package org.mav.example.payments.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.mav.example.payments.api.dto.BalanceSummaryDto;
import org.mav.example.payments.api.dto.PaymentRequest;
import org.mav.example.payments.client.ProductsClient;
import org.mav.example.payments.domain.Payment;
import org.mav.example.payments.service.PaymentService;
import org.mav.example.products.api.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentsController {

    private final ProductsClient productsClient;

    private final PaymentService paymentService;

    public PaymentsController(ProductsClient productsClient, PaymentService paymentService) {
        this.productsClient = productsClient;
        this.paymentService = paymentService;
    }

    /** Создать новый платёж */
    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment pay(@RequestBody PaymentRequest request) {
        return paymentService.execute(request);
    }

    /** Получить платёж по id */
    @GetMapping("/{id}")
    public Payment getById(@PathVariable @NotNull @Min(1) Long id) {
        return paymentService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "payment not found"));
    }

    /** Все платежи пользователя */
    @GetMapping("/by-user")
    public List<Payment> byUser(@RequestParam @NotNull @Min(1) Long userId) {
        return paymentService.findAllByUserId(userId);
    }


    @GetMapping("/products")
    public List<ProductDto> productsByUser(@RequestParam @NotNull @Min(1) @Max(10) Long userId) {
        return productsClient.findByUser(userId);
    }

    @GetMapping("/products/{id}")
    public ProductDto productById(@PathVariable @NotNull @Min(1) Long id) {
        return productsClient.findById(id);
    }

    @GetMapping("/balances/summary")
    public BalanceSummaryDto balanceSummary(@RequestParam @NotNull @Min(1) Long userId) {
        var products = productsClient.findByUser(userId);

        var total = products.stream()
                .map(ProductDto::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var byType = products.stream()
                .collect(Collectors.groupingBy(
                        ProductDto::type,
                        Collectors.mapping(ProductDto::balance,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return new BalanceSummaryDto(userId, total, byType);
    }
}