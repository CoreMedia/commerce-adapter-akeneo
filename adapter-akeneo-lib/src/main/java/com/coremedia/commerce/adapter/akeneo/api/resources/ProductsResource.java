package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedProductsEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.coremedia.commerce.adapter.akeneo.repositories.CategoryRepositoryImpl.UNCLASSIFIED_CATEGORY_ID;

@Service("akeneoProductsResource")
public class ProductsResource extends AbstractAkeneoApiResource {

  private static final String PRODUCTS_PATH = "/products";
  private static final String PRODUCT_BY_CODE_PATH = PRODUCTS_PATH + "/{" + CODE_PARAM + "}";

  public ProductsResource(AkeneoApiConnector connector) {
    super(connector);
  }

  @Cacheable("products")
  public Optional<ProductEntity> getProductByCode(String code) {
    Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
    return connector.getResource(PRODUCT_BY_CODE_PATH, pathParameters, ProductEntity.class);
  }

  @Cacheable("productsInCategory")
  public List<ProductEntity> getProductsInCategory(String categoryCode) {
    try {
      Filter searchFilter;
      if (UNCLASSIFIED_CATEGORY_ID.equals(categoryCode) || StringUtils.isBlank(categoryCode)) {
        // Get unclassified products
        searchFilter = FilterBuilder.newInstance().onProperty("categories").withOperator(Filter.Operator.UNCLASSIFIED).build();
      } else {
        // Filter products by categories classification
        searchFilter = FilterBuilder.newInstance().onProperty("categories").withOperator(Filter.Operator.IN).withValue(List.of(categoryCode)).build();
      }
      return searchProducts(searchFilter);

    } catch (Exception e) {
      System.out.println(e);
      return Collections.emptyList();
    }
  }

  public List<ProductEntity> searchProducts(Filter filter) {
    return performSearch(PRODUCTS_PATH, filter, PaginatedProductsEntity.class);
  }

}
