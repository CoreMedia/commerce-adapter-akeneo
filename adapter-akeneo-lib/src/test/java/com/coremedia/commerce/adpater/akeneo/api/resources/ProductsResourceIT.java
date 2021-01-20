package com.coremedia.commerce.adpater.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductsResource;
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
        ProductsResource.class
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

}
