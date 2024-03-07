package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedCategoriesEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.google.common.collect.ImmutableMap;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("akeneoCategoriesResource")
public class CategoriesResource extends AbstractAkeneoApiResource {

  private static final String CATEGORIES_PATH = "/categories";
  private static final String CATEGORY_BY_CODE_PATH = CATEGORIES_PATH + "/{" + CODE_PARAM + "}";

  public static final String UNCLASSIFIED_CATEGORY_ID = "_unclassified";

  public CategoriesResource(AkeneoApiConnector connector) {
    super(connector);
  }

  @Cacheable("categories")
  public Optional<CategoryEntity> getCategoryByCode(String code) {
    Optional<CategoryEntity> category;

    if (UNCLASSIFIED_CATEGORY_ID.equals(code)) {
      Filter rootCatagoryFilter = FilterBuilder.newInstance().onProperty("is_root").withOperator(Filter.Operator.EQUALS).withValue(true).build();
      List<CategoryEntity> categoryEntities = searchCategories(rootCatagoryFilter);
      category = categoryEntities.stream().findFirst();
    } else {
      Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
      category = connector.getResource(CATEGORY_BY_CODE_PATH, pathParameters, CategoryEntity.class);
    }

    return category;
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
  @Cacheable("childCategories")
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
