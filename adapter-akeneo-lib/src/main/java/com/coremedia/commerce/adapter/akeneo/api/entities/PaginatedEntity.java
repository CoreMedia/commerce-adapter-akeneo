package com.coremedia.commerce.adapter.akeneo.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class PaginatedEntity<T> extends AbstractEntity {

  private static final String SELF = "self";
  private static final String HREF = "href";
  private static final String FIRST = "first";
  private static final String NEXT = "next";
  private static final String PREVIOUS = "previous";
  private static final String ITEMS = "items";

  private String self;
  private String first;
  private String next;
  private String previous;

  private List<T> items;

  @JsonProperty("current_page")
  private int currentPage;

  @JsonProperty("_links")
  private void unpackPageLinks(Map<String, Map<String, String>> links) {
    this.self = Optional.of(links).map(l -> l.get(SELF)).map(l -> l.get(HREF)).orElse(null);
    this.first = Optional.of(links).map(l -> l.get(FIRST)).map(l -> l.get(HREF)).orElse(null);
    this.next = Optional.of(links).map(l -> l.get(NEXT)).map(l -> l.get(HREF)).orElse(null);
    this.previous = Optional.of(links).map(l -> l.get(PREVIOUS)).map(l -> l.get(HREF)).orElse(null);
  }

  @JsonProperty("_embedded")
  private void unpackItems(Map<String, Object> embedded) {
    final ObjectMapper mapper = new ObjectMapper();

    List<T> unpackedItems = Optional.of(embedded)
            .map(m -> m.get(ITEMS))
            .filter(List.class::isInstance)
            .map(List.class::cast)
            .map(l -> l.stream()
                    .map(m -> mapper.convertValue(m, getTargetItemType()))
                    .collect(Collectors.toList())
            )
            .map(List.class::cast)
            .orElse(null);

    this.items = unpackedItems;
  }



  abstract Class<T> getTargetItemType();

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public String getPrevious() {
    return previous;
  }

  public void setPrevious(String previous) {
    this.previous = previous;
  }

  public List<T> getItems() {
    return items;
  }

  public void setItems(List<T> items) {
    this.items = items;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }
}
