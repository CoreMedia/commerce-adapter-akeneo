package com.coremedia.commerce.adapter.akeneo.repositories;

import com.coremedia.commerce.adapter.base.entities.Catalog;
import com.coremedia.commerce.adapter.base.entities.EntityParams;
import com.coremedia.commerce.adapter.base.entities.ExternalId;
import com.coremedia.commerce.adapter.base.entities.Id;
import com.coremedia.commerce.adapter.base.entities.IdQuery;
import com.coremedia.commerce.adapter.base.repositories.CatalogRepository;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;

@DefaultAnnotation(NonNull.class)
@Repository
public class CatalogRepositoryImpl implements CatalogRepository {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private Catalog masterCatalog;

  public CatalogRepositoryImpl() {
    ExternalId id = ExternalId.of("master_catalog");
    Id rootCategoryId = ExternalId.of("master");
    masterCatalog = Catalog.builder(id, "Master Catalog", rootCategoryId).build();
  }

  @Override
  public Optional<Catalog> getCatalogById(IdQuery idQuery) {
    return Optional.of(masterCatalog);
  }

  @Override
  public List<Catalog> getCatalogs(EntityParams entityParams) {
    return List.of(masterCatalog);
  }

}
