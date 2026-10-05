package com.example.repository;

import com.example.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
@Testcontainers
public class ProductRepositoryContainerIT {

    @Container
    static ElasticsearchContainer container =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", container::getHttpHostAddress);
    }

    @Autowired
    private ProductRepository repository;

    @Test
    @DisplayName("[Repo Test] Should search by category in ES container")
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
