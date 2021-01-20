package com.coremedia.commerce.adapter.akeneo;

import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.nullToEmpty;

public class AkeneoApiConnector {

  private static final Logger LOG = LoggerFactory.getLogger(AkeneoApiConnector.class);

  private final String protocol;
  private final String host;
  private final String port;
  private final String basePath;
  private final String apiVersion;
  private final RestTemplate restTemplate;

  public AkeneoApiConnector(AkeneoApiConfigurationProperties properties,
                            RestTemplate restTemplate) {
    this.protocol = properties.getProtocol();
    this.host = properties.getHost();
    this.port = properties.getPort();
    this.basePath = properties.getBasePath();
    this.apiVersion = properties.getVersion();
    this.restTemplate = restTemplate;
  }

  // --- GET ---

  public <T> Optional<T> getResource(String resourcePath, Class<T> responseType) {
    return getResource(resourcePath, Collections.emptyMap(), ImmutableListMultimap.of(), responseType, false);
  }

  public <T> Optional<T> getResource(String resourcePath, Map<String, String> pathParams, Class<T> responseType) {
    return getResource(resourcePath, pathParams, ImmutableListMultimap.of(), responseType, false);
  }

  public <T> Optional<T> getResource(String resourcePath, Map<String, String> pathParams,
                                     ListMultimap<String, String> queryParams, Class<T> responseType) {
    return getResource(resourcePath, pathParams, queryParams, responseType, false);
  }

  public <T> Optional<T> getResourceByPaginationUrl(String absoluteResourcePageUrl, Class<T> responseType) {
    try {
      MultiValueMap<String, String> rawParams = UriComponentsBuilder.fromUriString(absoluteResourcePageUrl).build().getQueryParams();
      ListMultimap<String, String> queryParams = ArrayListMultimap.create();

      // Need to decode parameters here to avoid double encoding
      rawParams.toSingleValueMap().forEach((key, value) -> {
        String decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8);
        queryParams.put(key, decodedValue);
      });

      String url = UriComponentsBuilder.fromUriString(absoluteResourcePageUrl).replaceQuery(null).build().toString();
      return getResource(url, Collections.emptyMap(), queryParams, responseType, true);

    } catch (Exception e) {
      LOG.error("Unable to fetch paginated resource.", e);
      return Optional.empty();
    }
  }

  public <T> Optional<T> getResource(String resourcePath, Map<String, String> pathParams,
                                     ListMultimap<String, String> queryParams, Class<T> responseType, boolean isPaginatedPageRequest) {
    requireNonEmptyResourcePath(resourcePath);

    String url = buildRequestTemplateUrl(resourcePath, queryParams.keySet(), isPaginatedPageRequest);
    HttpEntity<String> requestEntity = new HttpEntity<>(buildHttpHeaders());
    Map<String, String> urlParams = mergeUrlParams(pathParams, queryParams);

    return performRequest(HttpMethod.GET, url, requestEntity, responseType, urlParams);
  }


  // --- POST ---

  public <T> Optional<T> postResource(String resourcePath, Map<String, String> pathParams, @Nullable String requestBody,
                                      Class<T> responseType) {
    return postResource(resourcePath, pathParams, ImmutableListMultimap.of(), requestBody, responseType);
  }

  public <T> Optional<T> postResource(String resourcePath, Map<String, String> pathParams,
                                      ListMultimap<String, String> queryParams, @Nullable String requestBody,
                                      Class<T> responseType) {
    requireNonEmptyResourcePath(resourcePath);

    String url = buildRequestTemplateUrl(resourcePath, queryParams.keySet(), false);
    HttpEntity<String> requestEntity = new HttpEntity<>(nullToEmpty(requestBody), buildHttpHeaders());
    Map<String, String> urlParams = mergeUrlParams(pathParams, queryParams);


    return performRequest(HttpMethod.POST, url, requestEntity, responseType, urlParams);
  }

  private static void requireNonEmptyResourcePath(String resourcePath) {
    checkArgument(StringUtils.isNotBlank(resourcePath), "Cannot request empty resource path.");
  }

  @VisibleForTesting
  String buildRequestTemplateUrl(String resourcePath, Set<String> queryParamNames, boolean isPaginatedPageRequest) {

    UriComponentsBuilder uriBuilder;

    if (isPaginatedPageRequest) {
      uriBuilder = UriComponentsBuilder.fromUriString(resourcePath);
    } else {
      uriBuilder = UriComponentsBuilder.newInstance()
              .scheme(protocol)
              .host(host)
              .port(port)
              .path(basePath);

      // Append API version.
      String apiVersionPathSegment = apiVersion;
      if (StringUtils.isNotBlank(apiVersionPathSegment)) {
        uriBuilder.pathSegment(apiVersionPathSegment);
      }

      // Append resource path.
      uriBuilder.path(resourcePath);
    }

    // Append template query parameters.
    getDefaultQueryParams().keySet().forEach(name -> uriBuilder.queryParam(name, "{" + name + "}"));
    queryParamNames.forEach(name -> uriBuilder.queryParam(name, "{" + name + "}"));

    return uriBuilder.build().toString();
  }

  private <T> Optional<T> performRequest(HttpMethod httpMethod, String url, HttpEntity<String> requestEntity,
                                         Class<T> responseType, Map<String, String> urlParams) {
    try {
      ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, responseType, urlParams);

      T responseBody = responseEntity.getBody();
      return Optional.ofNullable(responseBody);
    } catch (HttpClientErrorException ex) {
      LOG.trace("Call to '{}' with params '{}' raised exception.", url, urlParams, ex);
      HttpStatus statusCode = ex.getStatusCode();
      switch (statusCode) {
        case NOT_FOUND: {
          LOG.trace("Returning empty result.");
          return Optional.empty();
        }
        case UNAUTHORIZED: {
          LOG.info("Retrying with a new access token ...");
          invalidateAccessToken();
          ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(buildHttpHeaders()), responseType, urlParams);
          return Optional.ofNullable(responseEntity.getBody());
        }
        default: {
          throw new RuntimeException(
                  String.format("REST call to '%s' with params '%s' failed. Exception: %s", url, urlParams, ex.getMessage()), ex);
        }
      }
    }
  }

  private Map<String, String> mergeUrlParams(Map<String, String> pathParams, ListMultimap<String, String> queryParams) {
    HashMap<String, String> urlParams = new HashMap<>(pathParams);
    getDefaultQueryParams().forEach(urlParams::put);
    queryParams.forEach(urlParams::put);
    return urlParams;
  }

  protected HttpHeaders buildHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    return headers;
  }

  protected ListMultimap<String, String> getDefaultQueryParams() {
    return ImmutableListMultimap.of();
  }

  public void invalidateAccessToken() {

  };

}
