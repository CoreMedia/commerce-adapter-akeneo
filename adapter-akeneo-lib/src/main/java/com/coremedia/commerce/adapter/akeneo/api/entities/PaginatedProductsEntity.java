package com.coremedia.commerce.adapter.akeneo.api.entities;

public class PaginatedProductsEntity extends PaginatedEntity<ProductEntity> {

  @Override
  Class<ProductEntity> getTargetItemType() {
    return ProductEntity.class;
  }

}
