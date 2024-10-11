package com.lamashkevich.productservice.mapper;

import com.lamashkevich.productservice.dto.CreateProductDto;
import com.lamashkevich.productservice.dto.ProductDto;
import com.lamashkevich.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(CreateProductDto productDto);
}
