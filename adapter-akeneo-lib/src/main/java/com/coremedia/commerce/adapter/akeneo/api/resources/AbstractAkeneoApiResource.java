package com.coremedia.commerce.adapter.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.AkeneoApiConnector;
import com.coremedia.commerce.adapter.akeneo.api.entities.AbstractEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedEntity;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAkeneoApiResource {

  protected AkeneoApiConnector connector;

  // Path parameters
  protected final static String CODE_PARAM = "code";

  // Query parameters
  protected final static String SEARCH = "search";
  protected final static String LIMIT = "limit";
  protected final static String MIN_LIMIT = "1";
  protected final static String DEFAULT_LIMIT = "10";
  protected final static String MAX_LIMIT = "100";

  public AbstractAkeneoApiResource(AkeneoApiConnector connector) {
    this.connector = connector;
  }

  public AkeneoApiConnector getConnector() {
    return connector;
  }

  @Autowired
  public void setConnector(AkeneoApiConnector connector) {
    this.connector = connector;
  }

  protected <S extends PaginatedEntity<T>, T extends AbstractEntity> List<T> performSearch(String resourcePath, Class<S> paginationType) {
    return performSearch(resourcePath, null, paginationType);
  }

  protected <S extends PaginatedEntity<T>, T extends AbstractEntity> List<T> performSearch(String resourcePath, Filter filter, Class<S> paginationType) {
    List<T> results = new ArrayList<>();

    ListMultimap<String, String> queryParams = ArrayListMultimap.create();
    queryParams.put(LIMIT, MAX_LIMIT);

    if (filter != null) {
      queryParams.put(SEARCH, filter.toString());
    }

    // initial request
    Optional<S> resource = connector.getResource(resourcePath, Collections.emptyMap(), queryParams, paginationType);

    if (resource.isPresent()) {
      S firstPage = resource.get();
      results.addAll(firstPage.getItems());
      fetchNextPage(firstPage, results, paginationType);
    }

    return results;
  }

  private <S extends PaginatedEntity<T>, T extends AbstractEntity> List<T> fetchNextPage(
          PaginatedEntity<T> currentPage,
          List<T> list,
          Class<S> paginationType) {
    if (currentPage != null && StringUtils.isNotBlank(currentPage.getNext())) {
      Optional<S> nextPage = connector.getResourceByPaginationUrl(currentPage.getNext(), paginationType);
      nextPage.ifPresent(r -> list.addAll(r.getItems()));
      return fetchNextPage(nextPage.get(), list, paginationType);
    } else {
      return list;
    }
  }

}
