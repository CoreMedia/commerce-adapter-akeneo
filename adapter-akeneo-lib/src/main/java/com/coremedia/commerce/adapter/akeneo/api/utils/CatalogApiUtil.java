package com.coremedia.commerce.adapter.akeneo.api.utils;

import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ChannelsResource;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CatalogApiUtil {

  private final ChannelsResource channelsResource;

  public CatalogApiUtil(ChannelsResource channelsResource) {
    this.channelsResource = channelsResource;
  }

  public boolean isRootCategoryCode(String categoryCode) {
    Set<String> rootCategoryCodes = channelsResource.listChannels().stream().map(ChannelEntity::getCategoryTree).collect(Collectors.toSet());
    return rootCategoryCodes.contains(categoryCode);
  }

}
