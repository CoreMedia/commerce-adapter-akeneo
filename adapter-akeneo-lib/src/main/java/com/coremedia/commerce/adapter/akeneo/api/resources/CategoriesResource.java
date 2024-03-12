package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.ChannelEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedCategoriesEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.CatalogApiUtil;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.google.common.collect.ImmutableMap;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
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

  private final ChannelsResource channelsResource;
  private final CatalogApiUtil catalogApiUtil;

  public CategoriesResource(AkeneoApiConnector connector, ChannelsResource channelsResource, CatalogApiUtil catalogApiUtil) {
    super(connector);
    this.channelsResource = channelsResource;
    this.catalogApiUtil = catalogApiUtil;
  }

  @Cacheable(value = "categories", key = "#categoryCode")
  public Optional<CategoryEntity> getCategoryByCode(@NonNull String categoryCode, @Nullable String channelCode) {
    Optional<CategoryEntity> category;

    if (catalogApiUtil.isRootCategoryCode(categoryCode)) {
      category = getRootCategoryForChannel(channelCode);
    } else {
      Map<String, String> pathParameters = ImmutableMap.of(CODE_PARAM, categoryCode);
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

    // Api also retrieves sub-sub-categories, so we need to filter to get only direct children.
    return searchCategories(filterByParentCategoryFilter)
            .stream()
            .filter(child -> parentCode.equals(child.getParent()))
            .collect(Collectors.toList());
  }

  public List<CategoryEntity> searchCategories(Filter filter) {
    return performSearch(CATEGORIES_PATH, filter, PaginatedCategoriesEntity.class);
  }

  /**
   * Returns a list of all root categories.
   *
   * @return
   */
  public List<CategoryEntity> getRootCategories() {
    Filter rootCatagoryFilter = FilterBuilder.newInstance().onProperty("is_root").withOperator(Filter.Operator.EQUALS).withValue(true).build();
    return searchCategories(rootCatagoryFilter);
  }

  /**
   * Returns an optional containing the root category of the provided channel.
   *
   * @param channelCode
   * @return
   */
  public Optional<CategoryEntity> getRootCategoryForChannel(String channelCode) {
    return channelsResource.getChannelByCode(channelCode)
            .map(ChannelEntity::getCategoryTree)
            .flatMap(rootCategoryCode -> getRootCategories().stream()
                    .filter(c -> c.getCode().equals(rootCategoryCode))
                    .findFirst());
  }

}
