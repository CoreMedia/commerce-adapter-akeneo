package com.coremedia.commerce.adapter.akeneo.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ChannelEntity extends AbstractEntity {

  /**
   * Channel code.
   */
  @JsonProperty("code")
  private String code;

  /**
   * Codes of activated locales for the channel.
   */
  @JsonProperty("locales")
  private List<String> locales;

  /**
   * Codes of activated currencies for the channel.
   */
  @JsonProperty("currencies")
  private List<String> currencies;

  /**
   * Code of the category tree linked to the channel.
   */
  @JsonProperty("category_tree")
  private String categoryTree;

  /**
   * Conversion unit codes
   * used to convert the values of the attribute `attributeCode`
   * when exporting via the channel.
   */
  @JsonProperty("conversion_units")
  private Map<String, Object> conversionUnits;

  /**
   * Channel labels.
   */
  @JsonProperty("labels")
  private Map<String, String> labels;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<String> getLocales() {
    return locales;
  }

  public void setLocales(List<String> locales) {
    this.locales = locales;
  }

  public List<String> getCurrencies() {
    return currencies;
  }

  public void setCurrencies(List<String> currencies) {
    this.currencies = currencies;
  }

  public String getCategoryTree() {
    return categoryTree;
  }

  public void setCategoryTree(String categoryTree) {
    this.categoryTree = categoryTree;
  }

  public Map<String, Object> getConversionUnits() {
    return conversionUnits;
  }

  public void setConversionUnits(Map<String, Object> conversionUnits) {
    this.conversionUnits = conversionUnits;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public void setLabels(Map<String, String> labels) {
    this.labels = labels;
  }
}
