package com.coremedia.commerce.adapter.akeneo.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class CategoryEntity extends AbstractEntity {

  /**
   * Category code.
   */
  @JsonProperty("code")
  private String code;

  /**
   * Category code of the parent's category.
   */
  @JsonProperty("parent")
  private String parent;

  @JsonProperty("labels")
  private Map<String, String> labels;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public void setLabels(Map<String, String> labels) {
    this.labels = labels;
  }
}
