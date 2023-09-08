package com.coremedia.commerce.adapter.akeneo.repositories;

import com.coremedia.commerce.adapter.akeneo.api.entities.ProductEntity;
import com.coremedia.commerce.adapter.akeneo.api.resources.ProductsResource;
import com.coremedia.commerce.adapter.base.entities.IdQuery;
import com.coremedia.commerce.adapter.base.entities.Price;
import com.coremedia.commerce.adapter.base.entities.PriceBuilder;
import com.coremedia.commerce.adapter.base.entities.UserContext;
import com.coremedia.commerce.adapter.base.repositories.PriceRepository;
import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static com.coremedia.commerce.adapter.base.entities.PriceType.LIST;
import static com.coremedia.commerce.adapter.base.entities.PriceType.OFFER;

@DefaultAnnotation(NonNull.class)
@Repository
public class PriceRepositoryImpl implements PriceRepository {

  private ProductsResource productsResource;

  public PriceRepositoryImpl(ProductsResource productsResource) {
    this.productsResource = productsResource;
  }

  @Override
  public List<Price> getStaticPrices(IdQuery idQuery) {
    ImmutableList.Builder<Price> builder = ImmutableList.builder();

    String productCode = idQuery.getId().getValue();
    Optional<ProductEntity> productEntity = productsResource.getProductByCode(productCode);
    productEntity.map(ProductEntity::getPrices).ifPresent(prices -> {
      prices.forEach((currencyCode, amount) -> {
        BigDecimal value = new BigDecimal(amount);
        Currency currency = Currency.getInstance(currencyCode);
        builder.add(Price.builder(value, LIST, currency).build());
        builder.add(Price.builder(value, OFFER, currency).build());
      });
    });

    return builder.build();
  }

  @Override
  public List<Price> getDynamicPrices(IdQuery idQuery, UserContext userContext) {
    return Collections.emptyList();
  }

}
