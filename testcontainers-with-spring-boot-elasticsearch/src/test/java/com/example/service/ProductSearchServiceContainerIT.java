package com.example.service;

import com.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class ProductSearchServiceContainerIT {

    @Container
    static ElasticsearchContainer container =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", container::getHttpHostAddress);
    }

    @Autowired
    private ProductSearchService service;

    @BeforeEach
    void setup() {
        service.delete("P202");
    }

    @Test
    void testSaveAndSearchContainerized() {
        Product p = new Product("P202", "Wireless Mouse", 40.0, "Accessories");
        service.save(p);

        List<Product> results = service.findByCategory("Accessories");
        assertThat(results).anyMatch(prod -> prod.getName().equals("Wireless Mouse"));
    }
}
