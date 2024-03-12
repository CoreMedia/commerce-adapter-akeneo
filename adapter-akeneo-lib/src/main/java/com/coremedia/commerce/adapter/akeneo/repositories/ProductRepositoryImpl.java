package com.coremedia.commerce.adapter.akeneo.repositories;

import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductsResource;
import com.coremedia.commerce.adapter.akeneo.api.utils.CatalogApiUtil;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import com.coremedia.commerce.adapter.base.entities.EntityQuery;
import com.coremedia.commerce.adapter.base.entities.ExternalId;
import com.coremedia.commerce.adapter.base.entities.Id;
import com.coremedia.commerce.adapter.base.entities.IdQuery;
import com.coremedia.commerce.adapter.base.entities.NonBlankStringValueObject;
import com.coremedia.commerce.adapter.base.entities.Product;
import com.coremedia.commerce.adapter.base.entities.ProductBuilder;
import com.coremedia.commerce.adapter.base.entities.SearchQuery;
import com.coremedia.commerce.adapter.base.entities.SearchResult;
import com.coremedia.commerce.adapter.base.repositories.ProductRepository;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.coremedia.commerce.adapter.akeneo.api.utils.Filter.Operator.CONTAINS;
import static java.lang.invoke.MethodHandles.lookup;

@DefaultAnnotation(NonNull.class)
@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private final ProductsResource productsResource;

  private final CatalogApiUtil catalogApiUtil;

  private final Locale defaultLocale;

  private final AkeneoApiConfigurationProperties properties;

  public ProductRepositoryImpl(ProductsResource productsResource, AkeneoApiConfigurationProperties properties, CatalogApiUtil catalogApiUtil) {
    this.productsResource = productsResource;
    this.properties = properties;
    this.catalogApiUtil = catalogApiUtil;
    this.defaultLocale = properties.getDefaultLocale();
  }

  @Override
  public Optional<Product> getProductById(IdQuery idQuery) {
    LOG.debug("Fetching product by id query: {}", idQuery);
    Optional<Product> product = productsResource.getProductByCode(idQuery.getId().getValue())
            .map(productEntity -> toProduct(productEntity, idQuery));
    if (LOG.isDebugEnabled()) {
      if (product.isPresent()) {
        LOG.debug("Found product for id query: {} -> {}", idQuery, product.get());
      } else {
        LOG.debug("No product found for id query: {}", idQuery);
      }
    }
    return product;
  }

  @Override
  public SearchResult search(SearchQuery searchQuery) {
    LOG.debug("Searching products for search query: {}", searchQuery);
    String searchTerm = searchQuery.getSearchTerm().orElse("");
    Filter searchFilter = FilterBuilder.newInstance().onProperty("identifier").withOperator(CONTAINS).withValue(searchTerm).build();
    List<ProductEntity> productEntities = productsResource.searchProducts(searchFilter);
    List<Id> productIds = productEntities.stream()
            .map(ProductEntity::getIdentifier)
            .map(ExternalId::of)
            .collect(Collectors.toList());
    SearchResult result = SearchResult.builder().setTotalCount(productEntities.size()).setEntityIds(productIds).build();
    if (LOG.isDebugEnabled()) {
      if (result.getTotalCount() > 0) {
        LOG.debug("Found {} products for search query: {}", result.getTotalCount(), searchQuery);
      } else {
        LOG.debug("No products found for search for search query: {}", searchQuery);
      }
    }
    return result;
  }


  private Product toProduct(ProductEntity productEntity, EntityQuery entityQuery) {
    try {
      String productIdentifier = productEntity.getIdentifier();
      ExternalId id = ExternalId.of(productIdentifier);
      Locale locale = entityQuery.getLocale().orElse(defaultLocale);

      String name = getEntityAttributeWithFallback(productEntity,
              properties.getEntityAttributeMapping().getProduct().getName(),
              locale, productEntity.getName(), String.class)
              .orElse(productIdentifier);

      String[] categories = productEntity.getCategories();

      // Calculate parent category id
      Optional<String> parentCategoryCode = Arrays.stream(categories).findFirst();
      if (parentCategoryCode.isEmpty()) {
        // Fallback to root category
        parentCategoryCode = Optional.of(entityQuery.getCatalogId()
                .map(NonBlankStringValueObject::getValue)
                .flatMap(catalogApiUtil::getRootCategoryCodeForChannel)
                .orElse("master"));
      }

      ExternalId parentCategoryId = ExternalId.of(parentCategoryCode.get());

      ProductBuilder productBuilder = Product.builder(id, name, parentCategoryId);

      // Add currency
      entityQuery.getCurrency().ifPresent(currency -> productBuilder.setCurrencyCode(currency.getCurrencyCode()));

      String defaultDescription = productEntity.getLocalizedValue("description", locale, String.class).orElse(null);

      Optional<String> localizedShortDescription = getEntityAttributeWithFallback(productEntity,
              properties.getEntityAttributeMapping().getProduct().getShortDescription(),
              locale, defaultDescription, String.class);

      Optional<String> localizedLongDescription = getEntityAttributeWithFallback(productEntity,
              properties.getEntityAttributeMapping().getProduct().getLongDescription(),
              locale, defaultDescription, String.class);

      // Add short description
      localizedShortDescription.ifPresent(productBuilder::setShortDescription);

      // Add long description
      localizedLongDescription.ifPresent(productBuilder::setLongDescription);

      // Add Images
      productEntity.getImage().ifPresent(imagePath -> {
        productBuilder.setDefaultImageUrl(properties.getMediaEndpoint() + "/thumbnail_small/" + imagePath);
        productBuilder.setThumbnailImageUrl(properties.getMediaEndpoint() + "/preview/" + imagePath);
      });

      // TODO: Set parent/master product or variant ids
      productEntity.getPrices();

      return productBuilder.build();

    } catch (Exception e) {
      LOG.error("Unable to create product:", e);
      return null;
    }

  }

  private <T> Optional<T> getEntityAttributeWithFallback(ProductEntity productEntity, String attributeName, Locale locale, T fallback, Class<T> resultType) {
    Optional<T> result = Optional.ofNullable(fallback);
    if (StringUtils.isNotBlank(attributeName)) {
      result = productEntity.getLocalizedValue(attributeName, locale, resultType);
    }
    return result;
  }

}
