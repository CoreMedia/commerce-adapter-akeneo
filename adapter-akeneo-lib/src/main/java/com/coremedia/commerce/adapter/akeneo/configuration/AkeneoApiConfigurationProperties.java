package com.coremedia.commerce.adapter.akeneo.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;

@ConfigurationProperties(prefix = "akeneo.api")
public class AkeneoApiConfigurationProperties {

  /**
   * Protocol used for OCAPI REST communication.
   */
  private String protocol = "https";

  /**
   * The full qualified hostname of the Akeneo instance.
   */
  private String host = "sandbox.akeneo.com";

  /**
   * The port used for REST communication with the commerce system.
   */
  private int port = 0;

  /**
   * The API base path.
   */
  private String basePath = "/api/rest";

  /**
   * REST API Version used for REST communication with the commerce system.
   */
  private String version = "v1";

  private String clientId;
  private String clientSecret;
  private String username;
  private String password;

  private Locale defaultLocale = Locale.US;

  private String mediaCachePath = "/media/cache";

  private AkeneoEntityAttributeMapping entityAttributeMapping;

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Locale getDefaultLocale() {
    return defaultLocale;
  }

  public void setDefaultLocale(Locale defaultLocale) {
    this.defaultLocale = defaultLocale;
  }

  public String getMediaCachePath() {
    return mediaCachePath;
  }

  public void setMediaCachePath(String mediaCachePath) {
    this.mediaCachePath = mediaCachePath;
  }

  public String getMediaEndpoint() {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
            .scheme(protocol)
            .host(host)
            .path(mediaCachePath);

    if (port > 0) {
      uriComponentsBuilder.port(port);
    }

    return uriComponentsBuilder.build().toString();
  }

  public AkeneoEntityAttributeMapping getEntityAttributeMapping() {
    return entityAttributeMapping;
  }

  public void setEntityAttributeMapping(AkeneoEntityAttributeMapping entityAttributeMapping) {
    this.entityAttributeMapping = entityAttributeMapping;
  }
}
