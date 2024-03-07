package com.coremedia.commerce.adapter.akeneo.repositories;

import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ChannelsResource;
import com.coremedia.commerce.adapter.base.entities.Catalog;
import com.coremedia.commerce.adapter.base.entities.CatalogBuilder;
import com.coremedia.commerce.adapter.base.entities.EntityParams;
import com.coremedia.commerce.adapter.base.entities.ExternalId;
import com.coremedia.commerce.adapter.base.entities.IdQuery;
import com.coremedia.commerce.adapter.base.repositories.CatalogRepository;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;

@DefaultAnnotation(NonNull.class)
@Repository
public class CatalogRepositoryImpl implements CatalogRepository {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private final ChannelsResource channelsResource;

  public CatalogRepositoryImpl(ChannelsResource channelsResource) {
    this.channelsResource = channelsResource;
  }

  @Override
  public Optional<Catalog> getCatalogById(IdQuery idQuery) {
    LOG.debug("Fetching catalog for id query: {}.", idQuery);
    Optional<Catalog> catalog = channelsResource.getChannelByCode(idQuery.getId().getValue()).map(this::toCatalog);
    if (LOG.isDebugEnabled()) {
      if (catalog.isPresent()) {
        LOG.debug("Found catalog for id query: {} -> {}", idQuery, catalog.get());
      } else {
        LOG.debug("No catalog found for id query: {}", idQuery);
      }
    }
    return catalog;
  }

  @Override
  public List<Catalog> getCatalogs(EntityParams entityParams) {
    LOG.debug("Fetching all catalogs.");
    List<Catalog> catalogs = channelsResource.listChannels().stream()
            .map(this::toCatalog)
            .collect(Collectors.toList());
    LOG.debug("Fetched {} catalogs: {}.", catalogs.size(), catalogs);
    return catalogs;
  }

  private Catalog toCatalog(ChannelEntity channelEntity) {
    String channelCode = channelEntity.getCode();
    ExternalId id = ExternalId.of(channelCode);
    ExternalId rootCategoryId = ExternalId.of(channelEntity.getCategoryTree());
    String name = channelEntity.getCode();
    CatalogBuilder catalogBuilder = Catalog.builder(id, name, rootCategoryId);
    return catalogBuilder.build();
  }

}
