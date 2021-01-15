package com.coremedia.commerce.adpater.akeneo.api.utils;

import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FilterTest {

  @Test
  public void testCategoryFilterByParentCategory() {
    JSONObject expectedFilter = new JSONObject("{\"parent\":[{\"operator\":\"=\",\"value\":\"categoryA\"}]}");
    Filter filter = FilterBuilder.newInstance().onProperty("parent").withOperator(Filter.Operator.EQUALS).withValue("categoryA").build();
    assertEquals(expectedFilter.toString(), filter.toString());
  }

  @Test
  public void testCategoryFilterByCatagoryCodes() {
    JSONObject expectedFilter = new JSONObject("{\"code\":[{\"operator\":\"IN\",\"value\":[\"category_code1\",\"category_code2\"]}]}");
    Filter filter = FilterBuilder.newInstance().onProperty("code").withOperator(Filter.Operator.IN).withValue(List.of("category_code1", "category_code2")).build();
    assertEquals(expectedFilter.toString(), filter.toString());
  }

}
