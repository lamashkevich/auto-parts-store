package com.lamashkevich.productservice.service;

import com.lamashkevich.productservice.dto.CreateProductDto;
import com.lamashkevich.productservice.dto.ProductDto;
import com.lamashkevich.productservice.dto.SearchFilter;
import com.lamashkevich.productservice.entity.Product;
import com.lamashkevich.productservice.exception.ProductNotFoundException;
import com.lamashkevich.productservice.mapper.ProductMapper;
import com.lamashkevich.productservice.repository.ProductRepository;
import com.lamashkevich.productservice.repository.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto create(CreateProductDto productDto) {
        log.info("Create product: {}", productDto);
        Product newProduct = productMapper.toProduct(productDto);
        Product product = productRepository.save(newProduct);
        return productMapper.toProductDto(product);
    }

    @Override
    public Slice<ProductDto> searchWithPagination(String query, int page, int size) {
        log.info("Search with pagination by query: {}", query);

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be >= 0 and size > 0");
        }

        Specification<Product> spec = Specification.where(ProductSpecification.filterByQuery(query));
        Slice<Product> productPage = productRepository.findAll(spec, PageRequest.of(page, size));

        return productPage.map(productMapper::toProductDto);
    }

    @Override
    public ProductDto getById(Long id) {
        log.info("Get product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto update(Long id, CreateProductDto productDto) {
        log.info("Update product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCode(productDto.code());
        product.setBrand(productDto.brand());

        return productMapper.toProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Delete inventory with id {}", id);
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> search(String query) {
        log.info("Search by query: {}", query);

        Specification<Product> spec = Specification.where(ProductSpecification.filterByQuery(query));

        return productRepository.findAll(spec)
                .stream()
                .map(productMapper::toProductDto)
                .toList();
    }

    @Override
    public List<ProductDto> searchByFilter(SearchFilter filter) {
        log.info("Search by filter: {}", filter);

        Specification<Product> spec = Specification.where(ProductSpecification.hasCode(filter.code()))
                .and(ProductSpecification.hasBrand(filter.brand()));

        if (filter.query() != null && !filter.query().isEmpty()) {
            spec = spec.and(ProductSpecification.filterByQuery(filter.query()));
        }

        return productRepository.findAll(spec)
                .stream()
                .map(productMapper::toProductDto)
                .toList();
    }
}
