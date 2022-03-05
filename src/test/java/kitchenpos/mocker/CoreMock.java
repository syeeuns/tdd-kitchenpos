package kitchenpos.mocker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderType;
import kitchenpos.domain.Product;


public class CoreMock {

  public static final String BURGER_1 = "싸이버거";
  public static final String BURGER_2 = "데리버거";
  public static final String NAME_OF_MENU_1 = "싸이버거 + 싸이버거";
  public static final String NAME_OF_MENU_2 = "데리버거 + 데리버거";
  public static final String NAME_OF_ORDER_TABLE = "1번";
  public static final String NAME_OF_MENU_GROUP = "추천메뉴";
  public static final String DELIVERY_ADDRESS = "서울특별시 삼성동 512";

  public static final long QUANTITY_OF_MENU_1 = 1L;
  public static final long QUANTITY_OF_MENU_2 = 1L;
  public static final long QUANTITY_OF_ORDER_LINE_ITEM_1 = 1L;
  public static final long QUANTITY_OF_ORDER_LINE_ITEM_2 = 1L;

  public static final BigDecimal PRICE_OF_BURGER_1 = BigDecimal.valueOf(5000);
  public static final BigDecimal PRICE_OF_BURGER_2 = BigDecimal.valueOf(6000);
  public static final BigDecimal PRICE_OF_MENU_1 = BigDecimal.valueOf(9000);
  public static final BigDecimal PRICE_OF_MENU_2 = BigDecimal.valueOf(10000);
  public static final BigDecimal NEGATIVE_PRICE = BigDecimal.valueOf(-1);
  public static final BigDecimal MAX_PRICE = BigDecimal.valueOf(Integer.MAX_VALUE);

  public static final LocalDateTime CURRENT_TIME = LocalDateTime.now();


  public static final Product PRODUCT_1 = new Product.Builder()
      .id(UUID.randomUUID())
      .name(BURGER_1)
      .price(PRICE_OF_BURGER_1)
      .build();

  public static final Product PRODUCT_2 = new Product.Builder()
      .id(UUID.randomUUID())
      .name(BURGER_2)
      .price(PRICE_OF_BURGER_2)
      .build();

  public static final List<Product> PRODUCT_LIST = List.of(PRODUCT_1, PRODUCT_2);


  public static final MenuGroup MENU_GROUP = new MenuGroup.Builder()
      .id(UUID.randomUUID())
      .name(NAME_OF_MENU_GROUP)
      .build();

  public static final MenuProduct MENU_PRODUCT_1 = new MenuProduct.Builder()
      .seq(1L)
      .product(PRODUCT_1)
      .quantity(QUANTITY_OF_MENU_1)
      .productId(PRODUCT_1.getId())
      .build();

  public static final MenuProduct MENU_PRODUCT_2 = new MenuProduct.Builder()
      .seq(2L)
      .product(PRODUCT_2)
      .quantity(QUANTITY_OF_MENU_2)
      .productId(PRODUCT_2.getId())
      .build();

  public static final Menu MENU_1 = new Menu.Builder()
      .id(UUID.randomUUID())
      .name(NAME_OF_MENU_1)
      .price(PRICE_OF_MENU_1)
      .menuGroup(MENU_GROUP)
      .displayed(true)
      .menuProducts(List.of(MENU_PRODUCT_1, MENU_PRODUCT_1))
      .menuGroupId(MENU_GROUP.getId())
      .build();

  public static final Menu MENU_2 = new Menu.Builder()
      .id(UUID.randomUUID())
      .name(NAME_OF_MENU_2)
      .price(PRICE_OF_MENU_2)
      .menuGroup(MENU_GROUP)
      .displayed(true)
      .menuProducts(List.of(MENU_PRODUCT_2, MENU_PRODUCT_2))
      .menuGroupId(MENU_GROUP.getId())
      .build();

  public static final List<Menu> MENU_LIST = List.of(MENU_1, MENU_2);

  public static final List<UUID> MENU_ID_LIST = List.of(MENU_1.getId(), MENU_2.getId());

  // OrderLineItem
  public static final OrderLineItem ORDER_LINE_ITEM_1 = new OrderLineItem.Builder()
      .seq(1L)
      .menu(MENU_1)
      .quantity(QUANTITY_OF_ORDER_LINE_ITEM_1)
      .menuId(MENU_1.getId())
      .price(MENU_1.getPrice())
      .build();

  public static final OrderLineItem ORDER_LINE_ITEM_2 = new OrderLineItem.Builder()
      .seq(2L)
      .menu(MENU_2)
      .quantity(QUANTITY_OF_ORDER_LINE_ITEM_2)
      .menuId(MENU_2.getId())
      .price(MENU_2.getPrice())
      .build();

  public static final List<OrderLineItem> ORDER_LINE_ITEM_LIST = List.of(ORDER_LINE_ITEM_1, ORDER_LINE_ITEM_2);

  // OrderTable
  public static final OrderTable ORDER_TABLE = new OrderTable.Builder()
      .id(UUID.randomUUID())
      .name(NAME_OF_ORDER_TABLE)
      .numberOfGuests(0)
      .empty(true)
      .build();

  // Order
  public static final Order EAT_IN_ORDER = new Order.Builder()
      .id(UUID.randomUUID())
      .type(OrderType.EAT_IN)
      .status(OrderStatus.WAITING)
      .orderDateTime(CURRENT_TIME)
      .orderLineItems(ORDER_LINE_ITEM_LIST)
      .orderTable(ORDER_TABLE)
      .orderTableId(ORDER_TABLE.getId())
      .build();


  public static final Order DELIVERY_ORDER = new Order.Builder()
      .id(UUID.randomUUID())
      .type(OrderType.DELIVERY)
      .status(OrderStatus.WAITING)
      .orderDateTime(CURRENT_TIME)
      .orderLineItems(ORDER_LINE_ITEM_LIST)
      .deliveryAddress(DELIVERY_ADDRESS)
      .build();

  public static final List<Order> ORDER_LIST = List.of(EAT_IN_ORDER, DELIVERY_ORDER);
}
