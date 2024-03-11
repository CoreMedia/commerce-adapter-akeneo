package com.coremedia.commerce.adpater.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ChannelsResource;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductsResource;
import com.coremedia.commerce.adapter.akeneo.api.utils.CatalogApiUtil;
import com.coremedia.commerce.adapter.akeneo.api.utils.Filter;
import com.coremedia.commerce.adapter.akeneo.api.utils.FilterBuilder;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoConnectorConfiguration;
import com.coremedia.commerce.adpater.akeneo.AbstractAkeneoApiIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest(classes = {
        AkeneoConnectorConfiguration.class,
        ProductsResource.class,
        ChannelsResource.class,
        CatalogApiUtil.class
})
public class ProductsResourceIT extends AbstractAkeneoApiIT {

  @Autowired
  private ProductsResource productsResource;

  @Test
  public void testGetProductByCode() {
    Optional<ProductEntity> product = productsResource.getProductByCode("AKNSTK");
    assertTrue(product.isPresent());
  }

  @Test
  public void testGetProductsInCategory() {
    List<ProductEntity> hits = productsResource.getProductsInCategory("webcams");
    assertEquals(96, hits.size());
  }

  @Test
  public void testSearchProductsUsingFilterBuilder() {
    Filter filter = FilterBuilder.newInstance().onProperty("identifier").withOperator(Filter.Operator.CONTAINS).withValue("CAZ2010").build();
    List<ProductEntity> results = productsResource.searchProducts(filter);
    assertEquals(2, results.size());
  }

}
