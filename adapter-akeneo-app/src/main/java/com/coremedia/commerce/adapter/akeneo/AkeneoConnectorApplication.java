package com.coremedia.commerce.adapter.akeneo;

import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        AkeneoApiConfigurationProperties.class
})
public class AkeneoConnectorApplication {

  public static void main(String[] args) {
    SpringApplication.run(AkeneoConnectorApplication.class, args);
  }

}
