package com.coremedia.commerce.adapter.akeneo.api.entities;

public class PaginatedCategoriesEntity extends PaginatedEntity<CategoryEntity> {

  @Override
  Class<CategoryEntity> getTargetItemType() {
    return CategoryEntity.class;
  }

}
