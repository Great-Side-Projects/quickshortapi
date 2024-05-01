package org.dev.quickshortapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class QuickShortApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickShortApiApplication.class, args);
    }

}