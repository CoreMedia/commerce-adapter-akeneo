package com.coremedia.commerce.adapter.akeneo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
  private String port = "";

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

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
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
}
