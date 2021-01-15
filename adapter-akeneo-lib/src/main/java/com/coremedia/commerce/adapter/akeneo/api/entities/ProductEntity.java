package com.coremedia.commerce.adapter.akeneo.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ProductEntity extends AbstractEntity {

  /**
   * Product identifier, i.e. the value of the only `pim_catalog_identifier` attribute.
   */
  @JsonProperty("identifier")
  private String identifier;

  /**
   * Whether the product is enabled.
   */
  @JsonProperty("enabled")
  private boolean enabled;

  /**
   * Family code from which the product inherits its attributes and attributes requirements.
   */
  @JsonProperty("family")
  private String family;

  /**
   * Codes of the categories in which the product is classified.
   */
  @JsonProperty("categories")
  private String[] categories;

  /**
   * Codes of the groups to which the product belongs.
   */
  @JsonProperty("groups")
  private String[] groups;

  /**
   * Code of the parent product model when the product is a variant.
   */
  @JsonProperty("parent")
  private String parent;

  @JsonProperty("values")
  private Map<String, Object> values;

  @JsonProperty("associations")
  private Map<String, Object> associations;

  @JsonProperty("quantified_associations")
  private Map<String, Object> quantifiedAssociations;

  /**
   * Date of creation.
   */
  @JsonProperty("created")
  private String created;

  /**
   * Date of the last update.
   */
  @JsonProperty("updated")
  private String updated;

  @JsonProperty("metadata")
  private Map<String, Object> metadata;

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getFamily() {
    return family;
  }

  public void setFamily(String family) {
    this.family = family;
  }

  public String[] getCategories() {
    return categories;
  }

  public void setCategories(String[] categories) {
    this.categories = categories;
  }

  public String[] getGroups() {
    return groups;
  }

  public void setGroups(String[] groups) {
    this.groups = groups;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public Map<String, Object> getValues() {
    return values;
  }

  public void setValues(Map<String, Object> values) {
    this.values = values;
  }

  public Map<String, Object> getAssociations() {
    return associations;
  }

  public void setAssociations(Map<String, Object> associations) {
    this.associations = associations;
  }

  public Map<String, Object> getQuantifiedAssociations() {
    return quantifiedAssociations;
  }

  public void setQuantifiedAssociations(Map<String, Object> quantifiedAssociations) {
    this.quantifiedAssociations = quantifiedAssociations;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }
}
