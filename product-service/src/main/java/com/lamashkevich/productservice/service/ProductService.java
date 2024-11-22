package com.lamashkevich.productservice.service;

import com.lamashkevich.productservice.dto.CreateProductDto;
import com.lamashkevich.productservice.dto.ProductDto;
import com.lamashkevich.productservice.dto.SearchFilter;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductService {
    ProductDto create(CreateProductDto productDto);

    Slice<ProductDto> searchWithPagination(String query, int page, int size);

    ProductDto getById(Long id);

    ProductDto update(Long id, CreateProductDto productDto);

    void deleteById(Long id);

    List<ProductDto> search(String query);

    List<ProductDto> searchByFilter(SearchFilter filter);
}
