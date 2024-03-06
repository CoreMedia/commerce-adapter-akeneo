package com.coremedia.commerce.adpater.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ChannelsResource;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoConnectorConfiguration;
import com.coremedia.commerce.adpater.akeneo.AbstractAkeneoApiIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@SpringBootTest(classes = {
        AkeneoConnectorConfiguration.class,
        ChannelsResource.class
})
public class ChannelsResourceIT extends AbstractAkeneoApiIT {

  @Autowired
  private ChannelsResource channelsResource;

  @Test
  public void testListCategories() {
    List<ChannelEntity> channelList = channelsResource.listChannels();
    assertNotNull(channelList);
    assertEquals("Expected number of channels does not match.", 5, channelList.size());
  }

  @Test
  public void testGetChannelByCode() {
    Optional<ChannelEntity> channel = channelsResource.getChannelByCode("ecommerce");
    assertTrue(channel.isPresent());
    assertEquals("ecommerce", channel.get().getCode());
    assertEquals("master", channel.get().getCategoryTree());
  }


}
