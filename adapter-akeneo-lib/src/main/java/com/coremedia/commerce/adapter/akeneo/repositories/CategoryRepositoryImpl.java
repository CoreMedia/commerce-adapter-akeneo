package com.coremedia.commerce.adapter.akeneo.repositories;


import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.CategoriesResource;
import com.coremedia.commerce.adapter.akeneo.api.resources.ChannelsResource;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductsResource;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import com.coremedia.commerce.adapter.base.entities.Category;
import com.coremedia.commerce.adapter.base.entities.CategoryBuilder;
import com.coremedia.commerce.adapter.base.entities.EntityParams;
import com.coremedia.commerce.adapter.base.entities.EntityQuery;
import com.coremedia.commerce.adapter.base.entities.ExternalId;
import com.coremedia.commerce.adapter.base.entities.Id;
import com.coremedia.commerce.adapter.base.entities.IdQuery;
import com.coremedia.commerce.adapter.base.entities.NonBlankStringValueObject;
import com.coremedia.commerce.adapter.base.entities.SeoSegmentQuery;
import com.coremedia.commerce.adapter.base.repositories.CategoryRepository;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;

@DefaultAnnotation(NonNull.class)
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private final CategoriesResource categoriesResource;

  private final ChannelsResource channelsResource;

  private final ProductsResource productsResource;
  private final Locale defaultLocale;

  public CategoryRepositoryImpl(CategoriesResource categoriesResource, ProductsResource productsResource, AkeneoApiConfigurationProperties properties) {
    this.categoriesResource = categoriesResource;
    this.channelsResource = null;
    this.productsResource = productsResource;
    this.defaultLocale = properties.getDefaultLocale();
  }

  @Override
  public Optional<Category> getCategoryById(IdQuery idQuery) {
    LOG.debug("Fetching category by id query: {}", idQuery);
    String categoryCode = idQuery.getId().getValue();
    String channelCode = idQuery.getCatalogId().map(NonBlankStringValueObject::getValue).orElse(null);
    Optional<Category> category = categoriesResource.getCategoryByCode(categoryCode, channelCode)
            .map(entity -> toCategory(entity, idQuery));
    if (LOG.isDebugEnabled()) {
      if (category.isPresent()) {
        LOG.debug("Found category for id query: {} -> {}", idQuery, category.get());
      } else {
        LOG.debug("No category found for id query: {}", idQuery);
      }
    }
    return category;
  }

  @Override
  public Optional<Category> getCategoryBySeoSegment(SeoSegmentQuery seoSegmentQuery) {
    LOG.debug("Fetching category by seo segment query: {}", seoSegmentQuery);
    String seoSegment = seoSegmentQuery.getSeoSegment();
    String channelCode = seoSegmentQuery.getCatalogId().map(NonBlankStringValueObject::getValue).orElse(null);
    Optional<Category> category = categoriesResource.getCategoryByCode(seoSegment, channelCode)
            .map(entity -> toCategory(entity, seoSegmentQuery));
    if (LOG.isDebugEnabled()) {
      if (category.isPresent()) {
        LOG.debug("Found category for seo segment query: {} -> {}", seoSegmentQuery, category.get());
      } else {
        LOG.debug("No category found for seo segment query: {}", seoSegmentQuery);
      }
    }
    return category;
  }

  @Override
  public Iterable<Category> getCategories(EntityParams entityParams) {
    // NOTE: Method is currently unused
    return Collections.emptyList();
  }

  private Category toCategory(CategoryEntity categoryEntity, EntityQuery entityQuery) {
    String categoryCode = categoryEntity.getCode();
    ExternalId id = ExternalId.of(categoryCode);
    Locale locale = entityQuery.getLocale().orElse(defaultLocale);
    String name = categoryEntity.getLabels().get(locale.getLanguage() + "_" + locale.getCountry());
    if (StringUtils.isBlank(name)) {
      name = categoryEntity.getLabels().get(defaultLocale.toLanguageTag());
    }

    if (StringUtils.isBlank(name)) {
      name = categoryCode;
    }

    CategoryBuilder categoryBuilder = Category.builder(id, name);

    // Add parent category id
    String parentCategoryCode = categoryEntity.getParent();
    if (StringUtils.isNotBlank(parentCategoryCode)) {
      categoryBuilder.setParentId(ExternalId.of(parentCategoryCode));
    }

    // Add subcategory ids
    List<Id> childIds = categoriesResource.getChildCategories(categoryCode).stream()
            .map(CategoryEntity::getCode)
            .sorted()
            .map(ExternalId::of)
            .collect(Collectors.toList());
    categoryBuilder.setChildIds(childIds);

    // Add assigned products
    Set<Id> productIds = productsResource.getProductsInCategory(categoryCode).stream()
            .map(ProductEntity::getIdentifier)
            .map(ExternalId::of)
            .collect(Collectors.toSet());

    /*if (StringUtils.isBlank(categoryEntity.getParent())) {
      // no parent category so also add all unclassified products to this root category
      Set<Id> unclassifiedProductIds = productsResource.getProductsInCategory(null).stream()
              .map(ProductEntity::getIdentifier)
              .map(ExternalId::of)
              .collect(Collectors.toSet());

      productIds.addAll(unclassifiedProductIds);
    }*/

    // Sanitize product ids
    List<Id> sanitizedProductIds = productIds.stream().filter(pid -> !pid.getValue().contains("/"))
            .collect(Collectors.toList());

    categoryBuilder.setProductIds(new ArrayList<>(sanitizedProductIds));

    Category category = categoryBuilder.build();
    LOG.info("Created category: {}", category);
    return category;
  }
}
