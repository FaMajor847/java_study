package org.mav.example.api.mapper;

import org.mav.example.api.dto.ProductDto;
import org.mav.example.domain.Product;

public final class ProductMapper {
    private ProductMapper() {}

    public static ProductDto toDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getAccountNumber(),
                p.getBalance(),
                p.getType()
        );
    }
}