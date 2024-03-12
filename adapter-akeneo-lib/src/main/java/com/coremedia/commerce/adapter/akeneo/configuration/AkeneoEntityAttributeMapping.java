package com.coremedia.commerce.adapter.akeneo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

public class AkeneoEntityAttributeMapping {

  private ProductEntityMapping product;

  public ProductEntityMapping getProduct() {
    return product;
  }

  public void setProduct(ProductEntityMapping product) {
    this.product = product;
  }

  public static class ProductEntityMapping {
    String name;
    String seoSegment;
    String title;
    String shortDescription;
    String longDescription;
    String metaDescription;
    String metaKeywords;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getSeoSegment() {
      return seoSegment;
    }

    public void setSeoSegment(String seoSegment) {
      this.seoSegment = seoSegment;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getShortDescription() {
      return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
      this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
      return longDescription;
    }

    public void setLongDescription(String longDescription) {
      this.longDescription = longDescription;
    }

    public String getMetaDescription() {
      return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
      this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
      return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
      this.metaKeywords = metaKeywords;
    }
  }

}
