package com.ambev;

import com.ambev.core.model.Product;
import com.ambev.core.repository.ProductRepository;
import com.ambev.core.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private static final String ID_PRODUTO = "1656";
    private static final String ID_PRODUTO_NOT_FOUND = "123";

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test02() {
            Product product = Product.builder()
                    .id(ID_PRODUTO)
                    .build();

            when(repository.findById(ID_PRODUTO)).thenReturn(Optional.of(product));

            Optional<Product> result = productService.getProductById(ID_PRODUTO);

            assertTrue(result.isPresent());
            assertEquals(ID_PRODUTO, result.get().getId());

            verify(repository).findById(ID_PRODUTO);
        }

        @Test
        void testGetProductById_NotFound() {
            when(repository.findById(ID_PRODUTO_NOT_FOUND)).thenReturn(Optional.empty());
            Optional<Product> result = productService.getProductById(ID_PRODUTO_NOT_FOUND);

            assertFalse(result.isPresent());
            verify(repository).findById(ID_PRODUTO_NOT_FOUND);
    }

    @Test
    public void test03() {
        Product product = Product.builder()
                .id(ID_PRODUTO)
                .name("Novo Produto")
                .price(100.0)
                .description("Novo produto criado")
                .quantityAvailable(10)
                .build();

        when(repository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation
                        .getArgument(0));

        Product result = productService.createProduct(product);
        assertEquals(ID_PRODUTO, result.getId());
        assertEquals("Novo Produto", result.getName());
        assertEquals(100.0, result.getPrice());
        assertEquals("Novo produto criado", result.getDescription());
        assertEquals(10, result.getQuantityAvailable());
    }

    @Test
    public void test04() {
        Product existingProduct = Product.builder()
                .id(ID_PRODUTO)
                .name("Produto X")
                .price(100.0)
                .description("Produto a ser atualizado")
                .quantityAvailable(10)
                .build();

        Product updatedProduct = Product.builder()
                .name("Novo Produto X")
                .price(150.0)
                .description("Produto X atualizado")
                .quantityAvailable(10)
                .build();

        when(repository.findById(ID_PRODUTO)).
                thenReturn(Optional.of(existingProduct));
        when(repository.save(any(Product.class))).
                thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(ID_PRODUTO, updatedProduct);

        assertNotNull(result);
        assertEquals("Novo Produto X", result.getName());
        assertEquals(150.0, result.getPrice());
        assertEquals("Produto X atualizado", result.getDescription());
        assertEquals(10, result.getQuantityAvailable());

        verify(repository).findById(ID_PRODUTO);
        verify(repository).save(existingProduct);
    }

    @Test
    public void test05() {
        Product product = Product.builder()
                .id(ID_PRODUTO)
                .build();

        when(repository.findById(ID_PRODUTO)).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(ID_PRODUTO);

        Boolean result = productService.deleteProduct(ID_PRODUTO);

        assertTrue(result);
        verify(repository).findById(ID_PRODUTO);
        verify(repository).deleteById(ID_PRODUTO);
    }
    @Test
    public void test06() {
        int QUANTIDADE_INICIAL = 10;
        int QUANTIDADE_DIMINUIDA = 3;
        int QUANTIDADE_FINAL = 7;

        Product product = Product.builder()
                .id(ID_PRODUTO)
                .name("Test Product")
                .price(100.0)
                .description("Test Description")
                .quantityAvailable(QUANTIDADE_FINAL)
                .build();

        when(repository.findById(ID_PRODUTO)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

         productService.decreaseQuantity(ID_PRODUTO, QUANTIDADE_DIMINUIDA);

        assertEquals(QUANTIDADE_INICIAL - QUANTIDADE_DIMINUIDA, product.getQuantityAvailable());
        verify(repository).findById(ID_PRODUTO);
        verify(repository).save(product);
    }
}


