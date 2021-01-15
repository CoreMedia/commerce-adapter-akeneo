package com.coremedia.commerce.adpater.akeneo.oauth;

import com.coremedia.commerce.adapter.akeneo.oauth.AccessToken;
import com.coremedia.commerce.adapter.akeneo.oauth.AkeneoAuthenticator;
import com.coremedia.commerce.adpater.akeneo.AbstractAkeneoApiIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class AkeneoAuthenticatorIT extends AbstractAkeneoApiIT {

  @Autowired
  AkeneoAuthenticator akeneoAuthenticator;

  @Test
  public void testRequestNewToken() {
    AccessToken accessToken = akeneoAuthenticator.getOAuthToken();
    assertNotNull(accessToken);
  }

}
