package com.coremedia.commerce.adpater.akeneo.api.resources;

import com.coremedia.commerce.adapter.akeneo.api.entities.CategoryEntity;
import com.coremedia.commerce.adapter.akeneo.api.entities.PaginatedCategoriesEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.CategoryResource;
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
        CategoryResource.class
})
public class CategoryResourceIT extends AbstractAkeneoApiIT {

  @Autowired
  private CategoryResource categoryResource;

  @Test
  public void testGetRootCategory() {
    Optional<CategoryEntity> master = categoryResource.getCategoryByCode("master");
    assertTrue(master.isPresent());
  }

  @Test
  public void testListCategories() {
    List<CategoryEntity> categoryList = categoryResource.listCategories();
    assertNotNull(categoryList);
    assertEquals("Expected number of categories does not match.", 168, categoryList.size());
  }

  @Test
  public void testGetChildCategories() {
    List<CategoryEntity> children = categoryResource.getChildCategories("master");
    assertNotNull(children);
    assertEquals(9, children.size());
  }

}
