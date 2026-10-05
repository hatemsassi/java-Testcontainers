package com.example.service;

import com.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductSearchServiceIT {

    @Autowired
    private ProductSearchService service;

    @BeforeEach
    void setup() {
        service.delete("P101");
    }

    @Test
    void testSaveAndSearchExternal() {
        Product p = new Product("P101", "LED Monitor", 250.0, "Displays");
        service.save(p);

        List<Product> results = service.findByCategory("Displays");
        assertThat(results).anyMatch(prod -> prod.getName().equals("LED Monitor"));
    }
}
