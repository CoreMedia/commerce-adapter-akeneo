package com.coremedia.commerce.adapter.akeneo.oauth;

import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class AkeneoAuthenticator implements ClientHttpRequestInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(AkeneoAuthenticator.class);

  private static final String OAUTH_TOKEN_PATH = "/api/oauth/v1/token";
  private final RestTemplate restTemplate;

  private String clientId;
  private String clientSecret;
  private String username;
  private String password;

  private String tokenRequestUrl;

  private AccessToken token;

  public AkeneoAuthenticator(AkeneoApiConfigurationProperties properties) {
    this.clientId = properties.getClientId();
    this.clientSecret = properties.getClientSecret();
    this.username = properties.getUsername();
    this.password = properties.getPassword();

    restTemplate = new RestTemplate();
    tokenRequestUrl = buildAuthenticationUrl(properties);
  }

  public AccessToken getOAuthToken() {
    if (token == null) {
      token = requestNewToken();
    }
    return token;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request,
                                      byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {
    request.getHeaders().setBearerAuth(getOAuthToken().getAccessToken());
    return execution.execute(request, body);
  }

  private AccessToken requestNewToken() {
    LOG.debug("Requesting new access token.");
    String auth = clientId + ":" + clientSecret;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    headers.add("Authorization", "Basic " + new String(encodedAuth, StandardCharsets.UTF_8));

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(
            Map.of(
                    "username", username,
                    "password", password,
                    "grant_type", "password"
            )
            , headers);

    try {
      ResponseEntity<AccessToken> responseEntity = restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, requestEntity, AccessToken.class);
      AccessToken token = responseEntity.getBody();
      LOG.debug("Fetched access token {}.", token);
      return token;

    } catch (HttpClientErrorException e) {
      LOG.debug("Unable to fetch access token.", e);
      return null;
    }
  }

  private String buildAuthenticationUrl(AkeneoApiConfigurationProperties properties) {
    return UriComponentsBuilder.newInstance()
            .scheme(properties.getProtocol())
            .host(properties.getHost())
            .port(properties.getPort())
            .path(OAUTH_TOKEN_PATH)
            .build().toString();
  }

}
