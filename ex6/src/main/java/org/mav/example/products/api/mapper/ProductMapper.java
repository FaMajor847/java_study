package org.mav.example.products.api.mapper;

import org.mav.example.products.api.dto.ProductDto;
import org.mav.example.products.domain.Product;

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