package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedCategoriesEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.CatalogApiUtil;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;

@Service("akeneoCategoriesResource")
public class CategoriesResource extends AbstractAkeneoApiResource {

  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private static final String CATEGORIES_PATH = "/categories";
  private static final String CATEGORY_BY_CODE_PATH = CATEGORIES_PATH + "/{" + CODE_PARAM + "}";

  private final CatalogApiUtil catalogApiUtil;

  public CategoriesResource(AkeneoApiConnector connector, CatalogApiUtil catalogApiUtil) {
    super(connector);
    this.catalogApiUtil = catalogApiUtil;
  }

  @Cacheable("categories")
  public Optional<CategoryEntity> getCategoryByCode(String code) {
    Optional<CategoryEntity> category;

    if (catalogApiUtil.isRootCategoryCode(code)) {
      category = getRootCategory();
    } else {
      Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, code);
      category = connector.getResource(CATEGORY_BY_CODE_PATH, pathParameters, CategoryEntity.class);
    }

    return category;
  }

  public List<CategoryEntity> listCategories() {
    return performSearch(CATEGORIES_PATH, PaginatedCategoriesEntity.class);
  }

  public Optional<CategoryEntity> getRootCategory() {
    Filter rootCatagoryFilter = FilterBuilder.newInstance().onProperty("is_root").withOperator(Filter.Operator.EQUALS).withValue(true).build();
    List<CategoryEntity> categoryEntities = searchCategories(rootCatagoryFilter);
    return categoryEntities.stream().findFirst();
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
