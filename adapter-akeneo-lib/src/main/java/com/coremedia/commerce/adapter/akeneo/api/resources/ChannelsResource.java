package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedChannelsEntity;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;

@Service("akeneoChannelsResource")
public class ChannelsResource extends AbstractAkeneoApiResource {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private static final String CHANNELS_PATH = "/channels";
  private static final String CHANNEL_BY_CODE_PATH = CHANNELS_PATH + "/{" + CODE_PARAM + "}";

  public ChannelsResource(AkeneoApiConnector connector) {
    super(connector);
  }

  @Cacheable(value = "channels", key = "#code")
  public Optional<ChannelEntity> getChannelByCode(String code) {
    Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
    return connector.getResource(CHANNEL_BY_CODE_PATH, pathParameters, ChannelEntity.class);
  }

  @Cacheable("channels_list")
  public List<ChannelEntity> listChannels() {
    return performSearch(CHANNELS_PATH, PaginatedChannelsEntity.class);
  }

}
