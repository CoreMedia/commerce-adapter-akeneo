package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedProductsEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("akeneoProductsResource")
public class ProductsResource extends AbstractAkeneoApiResource {

  private static final String PRODUCTS_PATH = "/products";
  private static final String PRODUCT_BY_CODE_PATH = PRODUCTS_PATH + "/{" + CODE_PARAM + "}";

  public ProductsResource(AkeneoApiConnector connector) {
    super(connector);
  }

  public Optional<ProductEntity> getProductByCode(String code) {
    Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
    return connector.getResource(PRODUCT_BY_CODE_PATH, pathParameters, ProductEntity.class);
  }

  public List<ProductEntity> getProductsInCategory(String categoryCode) {
    Filter filterProductsByParentCategoryFilter = FilterBuilder.newInstance().onProperty("categories").withOperator(Filter.Operator.IN).withValue(List.of(categoryCode)).build();
    return searchProducts(filterProductsByParentCategoryFilter);
  }

  public List<ProductEntity> searchProducts(Filter filter) {
    return performSearch(PRODUCTS_PATH, filter, PaginatedProductsEntity.class);
  }

}
