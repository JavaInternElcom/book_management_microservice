package com.elcom.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.elcom.library.elasticsearch.repository")
@EntityScan(basePackages = "com.elcom.library.model")
@EnableJpaRepositories(basePackages = "com.elcom.library.repository")
public class LibraryServiceApplication {
    public static void main(String[] args) {
        // Fix lỗi "UDP failed setting ip_ttl | Method not implemented" khi start app trên Windows
        System.setProperty("java.net.preferIPv4Stack", "true");

        SpringApplication.run(LibraryServiceApplication.class, args);

    }
}
