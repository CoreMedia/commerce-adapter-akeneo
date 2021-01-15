package com.coremedia.commerce.adapter.akeneo.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private int expiresIn;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("refresh_token")
  private String refreshToken;


  private long createdAt;

  public AccessToken() {
    createdAt = System.currentTimeMillis();
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Checks whether or not the token is expired.
   *
   * @return <code>true</code> if the token is expired, <code>false</code> otherwise.
   */
  public boolean isExpired() {
    return System.currentTimeMillis() - createdAt > (expiresIn - 10) * 1000; // expire 10 seconds early
  }

  /**
   * Returns the HTTP-Header token value that can be used in an Authorization header.
   *
   * @return the HTTP-Header token value that can be used in an Authorization header.
   */
  public String toHttpHeaderValue() {
    return tokenType + " " + accessToken;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
            "token=" + accessToken +
            ", type=" + tokenType +
            ", scope=" + scope +
            ", expired=" + isExpired() +
            "}";
  }

}
