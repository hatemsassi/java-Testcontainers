package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import com.example.model.Product;

/**
 * Test runs against external Elasticsearch (localhost:9200).
 * Ensure Elasticsearch is running before executing.
 */
@DataElasticsearchTest
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void clearIndex() {
        repository.deleteAll(); // ensures a clean state before each test
    }

    @Test
    @DisplayName("[Repo Test] Should search by category in external ES")
    void testFindByCategory() {
        Product p1 = new Product("Keyboard", 120.0, "Accessories");
        Product p2 = new Product("Mouse", 40.0, "Accessories");
        Product p3 = new Product("Monitor", 350.0, "Displays");

        repository.saveAll(List.of(p1, p2, p3));

        List<Product> accessories = repository.findByCategory("Accessories");
        assertThat(accessories).hasSize(2);

        List<Product> displays = repository.findByCategory("Displays");
        assertThat(displays).hasSize(1);
    }
}
