package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedCategoriesEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryResource extends AbstractAkeneoApiResource {

  private static final String CATEGORIES_PATH = "/categories";
  private static final String CATEGORY_BY_CODE_PATH = CATEGORIES_PATH + "/{" + CODE_PARAM + "}";

  public CategoryResource(AkeneoApiConnector connector) {
    super(connector);
  }

  public Optional<CategoryEntity> getCategoryByCode(String code) {
    Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
    return connector.getResource(CATEGORY_BY_CODE_PATH, pathParameters, CategoryEntity.class);
  }

  public List<CategoryEntity> listCategories() {
    return performSearch(CATEGORIES_PATH, PaginatedCategoriesEntity.class);
  }

  /**
   * Get direct child categories of the provided parent category.
   *
   * @param parentCode
   * @return
   */
  public List<CategoryEntity> getChildCategories(String parentCode) {
    Filter filterByParentCategoryFilter = FilterBuilder.newInstance()
            .onProperty("parent")
            .withOperator(Filter.Operator.EQUALS)
            .withValue(parentCode)
            .build();

    // Api also retrieves sub-sub-categories so we need to filter to get only direct children.
    return searchCategories(filterByParentCategoryFilter)
            .stream()
            .filter(child -> parentCode.equals(child.getParent()))
            .collect(Collectors.toList());
  }

  public List<CategoryEntity> searchCategories(Filter filter) {
    return performSearch(CATEGORIES_PATH, filter, PaginatedCategoriesEntity.class);
  }

}
