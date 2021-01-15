package com.coremedia.commerce.adapter.akeneo.api.utils;

public class FilterBuilder {

  private String property;
  private Filter.Operator operator;
  private Object value;

  protected FilterBuilder() {
    property = "";
    operator = Filter.Operator.EQUALS;
    value = null;
  }

  public static FilterBuilder newInstance() {
    return new FilterBuilder();
  }

  public FilterBuilder onProperty(String property) {
    this.property = property;
    return this;
  }

  public FilterBuilder withOperator(Filter.Operator operator) {
    this.operator = operator;
    return this;
  }

  public FilterBuilder withValue(Object value) {
    this.value = value;
    return this;
  }

  public Filter build() {
    return new Filter(property, operator, value);
  }

}
