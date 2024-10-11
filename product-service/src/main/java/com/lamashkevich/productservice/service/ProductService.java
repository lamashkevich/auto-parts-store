package com.lamashkevich.productservice.service;

import com.lamashkevich.productservice.dto.CreateProductDto;
import com.lamashkevich.productservice.dto.ProductDto;
import org.springframework.data.domain.Slice;

public interface ProductService {
    ProductDto create(CreateProductDto productDto);

    Slice<ProductDto> search(String query, int page, int size);

    ProductDto getById(Long id);

    ProductDto update(Long id, CreateProductDto productDto);

    void deleteById(Long id);
}
