package com.lamashkevich.productservice.controller;

import com.lamashkevich.productservice.dto.CreateProductDto;
import com.lamashkevich.productservice.dto.ProductDto;
import com.lamashkevich.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Slice<ProductDto> searchByQuery(@RequestParam String query,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return productService.search(query, page, size);
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public ProductDto create(@Valid @RequestBody CreateProductDto productDto) {
        return productService.create(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @Valid @RequestBody CreateProductDto productDto) {
        return productService.update(id, productDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }
}
