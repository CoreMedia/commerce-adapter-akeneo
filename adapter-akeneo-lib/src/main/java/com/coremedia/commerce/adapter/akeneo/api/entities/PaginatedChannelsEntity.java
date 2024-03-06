package com.coremedia.commerce.adapter.akeneo.api.entities;

public class PaginatedChannelsEntity extends PaginatedEntity<ChannelEntity> {

  @Override
  Class<ChannelEntity> getTargetItemType() {
    return ChannelEntity.class;
  }

}
