package kitchenpos.mocker;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class CoreMock2 {
  public static Product product = new Product.Builder()
    .id(UUID.randomUUID())
    .name("싸이버거")
    .price(BigDecimal.valueOf(5000))
    .build();

  public static MenuGroup menuGroup = new MenuGroup.Builder()
    .id(UUID.randomUUID())
    .name("추천메뉴")
    .build();

  public static MenuProduct menuProduct = new MenuProduct.Builder()
    .seq(1L)
    .product(product)
    .quantity(2L)
    .build();

  public static Menu menu = new Menu.Builder()
    .id(UUID.randomUUID())
    .name("싸이버거 + 싸이버거")
    .price(BigDecimal.valueOf(9000))
    .menuGroup(menuGroup)
    .displayed(true)
    .menuProducts(List.of(menuProduct))
    .build();
}
