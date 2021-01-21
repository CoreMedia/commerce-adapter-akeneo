package com.coremedia.commerce.adapter.akeneo.repositories;

import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductsResource;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import com.coremedia.commerce.adapter.base.entities.EntityQuery;
import com.coremedia.commerce.adapter.base.entities.ExternalId;
import com.coremedia.commerce.adapter.base.entities.IdQuery;
import com.coremedia.commerce.adapter.base.entities.Product;
import com.coremedia.commerce.adapter.base.entities.ProductBuilder;
import com.coremedia.commerce.adapter.base.entities.SearchQuery;
import com.coremedia.commerce.adapter.base.entities.SearchResult;
import com.coremedia.commerce.adapter.base.entities.SearchResultFacet;
import com.coremedia.commerce.adapter.base.repositories.ProductRepository;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;

@DefaultAnnotation(NonNull.class)
@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private final ProductsResource productsResource;

  private final Locale defaultLocale;

  private final AkeneoApiConfigurationProperties properties;

  public ProductRepositoryImpl(ProductsResource productsResource, AkeneoApiConfigurationProperties properties) {
    this.productsResource = productsResource;
    this.properties = properties;
    this.defaultLocale = properties.getDefaultLocale();
  }

  @Override
  public Optional<Product> getProductById(IdQuery idQuery) {
    return productsResource.getProductByCode(idQuery.getId().getValue())
            .map(productEntity -> toProduct(productEntity, idQuery));
  }

  @Override
  public List<SearchResultFacet> getFacetsForProductSearch(IdQuery idQuery) {
    return null;
  }

  @Override
  public SearchResult search(SearchQuery searchQuery) {
    return null;
  }


  private Product toProduct(ProductEntity productEntity, EntityQuery entityQuery) {
    String productIdentifier = productEntity.getIdentifier();
    ExternalId id = ExternalId.of(productIdentifier);
    Locale locale = entityQuery.getLocale().orElse(defaultLocale);
    String name = productEntity.getName();


    String parentCode = Optional.ofNullable(productEntity.getFamily()).orElse("master");
    ExternalId parentCategoryId = ExternalId.of(parentCode);
    ProductBuilder productBuilder = Product.builder(id, name, parentCategoryId);

    // Add currency
    entityQuery.getCurrency().ifPresent(currency -> productBuilder.setCurrencyCode(currency.getCurrencyCode()));

    // Add description
    productEntity.getLocalizedValue("description", locale, String.class)
            .ifPresent(description -> {
              productBuilder.setShortDescription(description);
              productBuilder.setLongDescription(description);
            });

    // Add Images
    productEntity.getImage().ifPresent(imagePath -> {
      productBuilder.setDefaultImageUrl(properties.getMediaEndpoint() + "/thumbnail_small/" + imagePath);
      productBuilder.setThumbnailImageUrl(properties.getMediaEndpoint() + "/preview/" + imagePath);
    });

    // TODO: Set parent/master product or variant ids
    productEntity.getPrices();

    return productBuilder.build();
  }

}
