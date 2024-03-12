package com.coremedia.commerce.adapter.akeneo.api.utils;

import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ChannelsResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CatalogApiUtil {

  private final ChannelsResource channelsResource;

  public CatalogApiUtil(ChannelsResource channelsResource) {
    this.channelsResource = channelsResource;
  }

  /**
   * Check if the provided category code refers to a root category.
   *
   * @param categoryCode category code
   * @return
   */
  public boolean isRootCategoryCode(String categoryCode) {
    Set<String> rootCategoryCodes = channelsResource.listChannels().stream().map(ChannelEntity::getCategoryTree).collect(Collectors.toSet());
    return rootCategoryCodes.contains(categoryCode);
  }

  /**
   * Returns an optional containing the root category id for the requested channel.
   *
   * @param channelCode channel code
   * @return
   */
  public Optional<String> getRootCategoryCodeForChannel(String channelCode) {
    return channelsResource.getChannelByCode(channelCode).map(ChannelEntity::getCategoryTree);
  }

}
