package com.coremedia.commerce.adpater.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductResource;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoConnectorConfiguration;
import com.coremedia.commerce.adpater.akeneo.AbstractAkeneoApiIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest(classes = {
        AkeneoConnectorConfiguration.class,
        ProductResource.class
})
public class ProductResourceIT extends AbstractAkeneoApiIT {

  @Autowired
  private ProductResource productResource;

  @Test
  public void testGetProductByCode() {
    Optional<ProductEntity> product = productResource.getProductByCode("AKNSTK");
    assertTrue(product.isPresent());
  }

  @Test
  public void testGetProductsInCategory() {
    List<ProductEntity> hits = productResource.getProductsInCategory("webcams");
    assertEquals(96, hits.size());
  }

}
