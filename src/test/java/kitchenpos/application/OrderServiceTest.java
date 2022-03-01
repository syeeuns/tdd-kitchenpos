package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderType;
import kitchenpos.infra.KitchenridersClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

  public static final String DELIVERY_ADDRESS = "서울특별시 삼성동 512";

  OrderRepository orderRepository = mock(OrderRepository.class);
  MenuRepository menuRepository = mock(MenuRepository.class);
  OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
  KitchenridersClient kitchenridersClient = mock(KitchenridersClient.class);

  OrderService orderService = new OrderService(
      orderRepository, menuRepository, orderTableRepository, kitchenridersClient
  );

  private static Menu menu;
  private static Order basicOrder;
  private static Order deliveryOrder;
  private static Order eatInOrder;
  private static OrderLineItem orderLineItem;


  @BeforeAll
  static void setUp() {
    basicOrder = new Order(
        UUID.randomUUID(),
        null,
        // 테스트별도 유동적으로 넣어준다.
        null,
        LocalDateTime.now(),
        // TODO: 공통적으로 쓸 Menu, Order 등을 어딘가에 static final로 빼주기
        null,
        null,
        null
    );

    eatInOrder = new Order(basicOrder);
    eatInOrder.setType(OrderType.EAT_IN);

    deliveryOrder = new Order(basicOrder);
    deliveryOrder.setType(OrderType.DELIVERY);
  }

  @DisplayName("주문 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Order() {
    // 준비
    given(menuRepository.findAllById(any())).willReturn();
    given(menuRepository.findById(any())).willReturn();
    given(orderTableRepository.findById(any())).willReturn();
    given(orderRepository.save(any())).willReturn();

    // 실행
    Order newbie = orderService.create();

    //검증
    assertThat(newbie).isEqualTo();
  }
}
