package com.coremedia.commerce.adapter.akeneo.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Filter {

  private String property;
  private Operator operator;
  private Object value;

  public Filter(String property, Operator operator, Object value) {
    this.property = property;
    this.operator = operator;
    this.value = value;
  }

  public enum Operator {

    // Filtering on product properties
    IN("IN"),
    NOT_IN("NOT IN"),
    IN_OR_UNCLASSIFIED("IN OR UNCLASSIFIED"),
    IN_CHILDREN("IN CHILDREN"),
    NOT_IN_CHILDREN("NOT IN CHILDREN"),
    UNCLASSIFIED("UNCLASSIFIED"),

    EQUALS("="),
    NOT_EQUALS("!=");

    private String label;

    Operator(String label) {
      this.label = label;
    }

  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public JSONObject asJSON() {
    JSONArray jsonArray = new JSONArray();
    jsonArray.put(new JSONObject(Map.of("operator", operator.label, "value", value)));
    JSONObject json = new JSONObject();
    json.put(property, jsonArray);
    return json;
  }

  @Override
  public String toString() {
    return asJSON().toString();
  }
}
