package com.lamashkevich.productservice.service;

import com.lamashkevich.productservice.dto.CreateProductDto;
import com.lamashkevich.productservice.dto.ProductDto;
import com.lamashkevich.productservice.dto.SearchFilter;
import com.lamashkevich.productservice.entity.Product;
import com.lamashkevich.productservice.exception.ProductNotFoundException;
import com.lamashkevich.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    public void create_ShouldReturnCreatedProduct() {
        var dto = new CreateProductDto("code","brand","name","description");

        ProductDto createdProduct = productService.create(dto);

        assertNotNull(createdProduct);
        assertNotNull(createdProduct.id());
        assertEquals(dto.code(), createdProduct.code());
        assertEquals(dto.brand(), createdProduct.brand());
        assertEquals(dto.name(), createdProduct.name());
        assertEquals(dto.description(), createdProduct.description());
    }

    @Test
    public void getById_ShouldReturnProduct() {
        Product createdProduct = saveProduct("code", "brand", "name", "description");

        ProductDto product = productService.getById(createdProduct.getId());

        assertNotNull(product);
        assertEquals(createdProduct.getId(), product.id());
        assertEquals(createdProduct.getCode(), product.code());

    }

    @Test
    public void getById_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        assertThrows(ProductNotFoundException.class, () -> productService.getById(Long.MAX_VALUE));
    }

    @Test
    public void searchWithPagination_ShouldReturnProductSlice() {
        String query = "query";
        int page = 0;
        int size = 4;

        saveProduct("code1", "brand1", "name1", "description1");
        saveProduct("query2", "brand2", "name2", "description2");
        saveProduct("code3", "query3", "name3", "description3");
        saveProduct("code4", "brand4", "nameQuery4", "description4");
        saveProduct("code5", "brand5", "nameQuery5", "descriptionQUERY5");

        Slice<ProductDto> slice = productService.searchWithPagination(query, page, size);

        assertNotNull(slice);
        assertTrue(slice.isLast());
        assertEquals(4, slice.getNumberOfElements());
    }

    @Test
    public void searchWithPagination_ShouldThrowIllegalArgumentException_WhenSizeOrPageValueIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> productService.searchWithPagination("", 0, 0));
        assertThrows(IllegalArgumentException.class, () -> productService.searchWithPagination("", -1, 1));
    }

    @Test
    public void deleteById_ShouldDeleteProduct() {
        Product product = saveProduct("code", "brand", "name", "description");

        productService.deleteById(product.getId());

        assertFalse(productRepository.findById(product.getId()).isPresent());
    }

    @Test
    public void deleteById_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        assertThrows(ProductNotFoundException.class, () -> productService.deleteById(Long.MAX_VALUE));
    }

    @Test
    public void update_ShouldReturnProduct() {
        Product product = saveProduct("code1", "brand1", "name1", "description1");

        CreateProductDto updateDto = new CreateProductDto("code", "brand", "name", "description");
        ProductDto updatedProduct = productService.update(product.getId(), updateDto);

        assertNotNull(updatedProduct);
        assertEquals(product.getId(), updatedProduct.id());
        assertEquals("code", updatedProduct.code());
        assertEquals("brand", updatedProduct.brand());
        assertEquals("name", updatedProduct.name());
        assertEquals("description", updatedProduct.description());
    }

    @Test
    public void update_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        assertThrows(ProductNotFoundException.class, () ->
                productService.update(Long.MAX_VALUE, any(CreateProductDto.class)));
    }

    @Test
    public void searchByFilter_ShouldReturnProductList_WhenQueryAndCodeIsEmpty() {
        saveProduct("code1", "brand", "name1", "description1");
        saveProduct("code2", "brand", "name2", "description2");
        saveProduct("code2", "brand", "name3", "description3");

        SearchFilter filter = new SearchFilter("", "code2", "");

        List<ProductDto> searched = productService.searchByFilter(filter);

        assertNotNull(searched);
        assertEquals(2, searched.size());
        assertEquals("code2", searched.getFirst().code());
        assertEquals("brand", searched.getFirst().brand());
    }

    @Test
    public void searchByFilter_ShouldReturnProductList_WhenQueryIsPresent() {
        saveProduct("code1", "brand", "name1", "description1");
        saveProduct("code2", "brand", "name2", "description2");
        saveProduct("code2", "brand", "name3", "description3");

        SearchFilter filter = new SearchFilter("3", "code2", "brand");

        List<ProductDto> searched = productService.searchByFilter(filter);

        assertNotNull(searched);
        assertEquals(1, searched.size());
        assertEquals("name3", searched.getFirst().name());
    }

    @Test
    public void search_ShouldReturnProductList_WhenQueryIsPresent() {
        saveProduct("code1", "brand", "name1", "description1");
        saveProduct("code2", "brand", "name2", "description2");
        saveProduct("code2", "brand", "name3", "description3");

        List<ProductDto> searched = productService.search("3");

        assertNotNull(searched);
        assertEquals(1, searched.size());
        assertEquals("name3", searched.getFirst().name());
    }

    @Test
    public void search_ShouldReturnProductList_WhenQueryIsEmpty() {
        saveProduct("code1", "brand", "name1", "description1");
        saveProduct("code2", "brand", "name2", "description2");
        saveProduct("code2", "brand", "name3", "description3");

        List<ProductDto> searched = productService.search("");

        assertNotNull(searched);
        assertEquals(3, searched.size());
    }

    private Product saveProduct(String code, String brand, String name, String description) {
        Product product = Product.builder()
                .brand(brand)
                .code(code)
                .name(name)
                .description(description)
                .build();
        return productRepository.save(product);
    }
}