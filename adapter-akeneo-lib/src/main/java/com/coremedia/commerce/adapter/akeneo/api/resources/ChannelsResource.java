package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedChannelsEntity;
import com.google.common.collect.ImmutableMap;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("akeneoChannelsResource")
public class ChannelsResource extends AbstractAkeneoApiResource {

  private static final String CHANNELS_PATH = "/channels";
  private static final String CHANNEL_BY_CODE_PATH = CHANNELS_PATH + "/{" + CODE_PARAM + "}";

  public ChannelsResource(AkeneoApiConnector connector) {
    super(connector);
  }

  @Cacheable("channels")
  public Optional<ChannelEntity> getChannelByCode(String code) {
    Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
    return connector.getResource(CHANNEL_BY_CODE_PATH, pathParameters, ChannelEntity.class);
  }

  public List<ChannelEntity> listChannels() {
    return performSearch(CHANNELS_PATH, PaginatedChannelsEntity.class);
  }

}
