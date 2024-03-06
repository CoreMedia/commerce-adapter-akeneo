package com.coremedia.commerce.adapter.akeneo.configuration;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.oauth.AkeneoAuthenticator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({
        AkeneoApiConfigurationProperties.class
})
public class AkeneoConnectorConfiguration {

  @Bean
  public AkeneoApiConnector akeneoApiConnector(AkeneoApiConfigurationProperties properties,
                                               AkeneoAuthenticator authenticator,
                                               RestTemplate restTemplate) {
    return new AkeneoApiConnector(properties, authenticator, restTemplate);
  }

  @Bean
  public AkeneoAuthenticator authenticator(AkeneoApiConfigurationProperties properties) {
    return new AkeneoAuthenticator(properties);
  }

  @Bean
  public RestTemplate restTemplate(AkeneoAuthenticator akeneoAuthenticator) {
    RestTemplate restTemplate = new RestTemplate();

    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
    if (CollectionUtils.isEmpty(interceptors)) {
      interceptors = new ArrayList<>();
    }
    interceptors.add(akeneoAuthenticator);
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }

}
