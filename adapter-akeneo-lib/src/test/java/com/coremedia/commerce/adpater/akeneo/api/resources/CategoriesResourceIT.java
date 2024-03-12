package com.coremedia.commerce.adpater.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.CategoriesResource;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoConnectorConfiguration;
import com.coremedia.commerce.adpater.akeneo.AbstractAkeneoApiIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@SpringBootTest(classes = {
        AkeneoConnectorConfiguration.class,
        CategoriesResource.class
})
public class CategoriesResourceIT extends AbstractAkeneoApiIT {

  @Autowired
  private CategoriesResource categoriesResource;

  @Test
  public void testGetRootCategory() {
    Optional<CategoryEntity> master = categoriesResource.getCategoryByCode("master", "Catalog");
    assertTrue(master.isPresent());
  }

  @Test
  public void testListCategories() {
    List<CategoryEntity> categoryList = categoriesResource.listCategories();
    assertNotNull(categoryList);
    assertEquals("Expected number of categories does not match.", 168, categoryList.size());
  }

  @Test
  public void testGetChildCategories() {
    List<CategoryEntity> children = categoriesResource.getChildCategories("master");
    assertNotNull(children);
    assertEquals(9, children.size());
  }

}
