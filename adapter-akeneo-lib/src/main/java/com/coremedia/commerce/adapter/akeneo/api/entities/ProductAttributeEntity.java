package com.coremedia.commerce.adapter.akeneo.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAttributeEntity extends AbstractEntity {

  /**
   * The code of a locale when the attribute is localizable,
   * should be equal to null otherwise.
   */
  @JsonProperty("locale")
  private String locale;

  /**
   * The code of a channel when the attribute is scopable,
   * should be equal to null otherwise.
   */
  @JsonProperty("scope")
  private String scope;

  /**
   * The value stored for this attribute for this locale (if attribute is localizable)
   * and this channel (if the attribute is scopable).
   * Its type and format depend on the attribute type.
   */
  @JsonProperty("data")
  private Object data;

  /**
   * Containing the attribute option labels if the attribute is a simple or multi select.
   */
  @JsonProperty("linked_data")
  private Object linkedData;

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public Object getLinkedData() {
    return linkedData;
  }

  public void setLinkedData(Object linkedData) {
    this.linkedData = linkedData;
  }
}
