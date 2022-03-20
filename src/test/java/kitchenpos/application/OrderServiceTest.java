package kitchenpos.application;

import static kitchenpos.mocker.CoreMock.DELIVERY_ORDER;
import static kitchenpos.mocker.CoreMock.EAT_IN_ORDER;
import static kitchenpos.mocker.CoreMock.MENU_1;
import static kitchenpos.mocker.CoreMock.MENU_2;
import static kitchenpos.mocker.CoreMock.MENU_ID_LIST;
import static kitchenpos.mocker.CoreMock.MENU_LIST;
import static kitchenpos.mocker.CoreMock.ORDER_LIST;
import static kitchenpos.mocker.CoreMock.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.infra.KitchenridersClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderServiceTest {

  OrderRepository orderRepository = mock(OrderRepository.class);
  MenuRepository menuRepository = mock(MenuRepository.class);
  OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
  KitchenridersClient kitchenridersClient = mock(KitchenridersClient.class);

  OrderService orderService = new OrderService(
      orderRepository, menuRepository, orderTableRepository, kitchenridersClient
  );


  @DisplayName("주문 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Order() {
    final OrderTable notEmptyOrderTable = new OrderTable(ORDER_TABLE);
    notEmptyOrderTable.setEmpty(false);

    // 준비
    given(menuRepository.findAllByIdIn(MENU_ID_LIST)).willReturn(MENU_LIST);
    given(menuRepository.findById(MENU_1.getId())).willReturn(Optional.of(MENU_1));
    given(menuRepository.findById(MENU_2.getId())).willReturn(Optional.of(MENU_2));
    given(orderTableRepository.findById(any())).willReturn(Optional.of(notEmptyOrderTable));
    given(orderRepository.save(any())).willReturn(EAT_IN_ORDER);

    // 실행
    Order newbie = orderService.create(EAT_IN_ORDER);

    //검증
    assertThat(newbie).isEqualTo(EAT_IN_ORDER);
  }

  @DisplayName("주문 접수 -> 성공")
  @Test
  void SHOULD_success_WHEN_accept_Order() {
    // 준비
    given(orderRepository.findById(EAT_IN_ORDER.getId()))
        .willReturn(Optional.of(EAT_IN_ORDER));

    // 실행
    Order newbie = orderService.accept(EAT_IN_ORDER.getId());

    //검증
    assertThat(newbie.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
  }

  @DisplayName("주문 서빙 -> 성공")
  @Test
  void SHOULD_success_WHEN_serve_Order() {
    // 준비
    final Order acceptedOrder = new Order(EAT_IN_ORDER);
    acceptedOrder.setStatus(OrderStatus.ACCEPTED);

    given(orderRepository.findById(acceptedOrder.getId()))
        .willReturn(Optional.of(acceptedOrder));

    // 실행
    Order newbie = orderService.serve(acceptedOrder.getId());

    //검증
    assertThat(newbie.getStatus()).isEqualTo(OrderStatus.SERVED);
  }

  @DisplayName("주문 배달 시작 -> 성공")
  @Test
  void SHOULD_success_WHEN_start_delivery_Order() {
    // 준비
    final Order servedOrder = new Order(DELIVERY_ORDER);
    servedOrder.setStatus(OrderStatus.SERVED);

    given(orderRepository.findById(servedOrder.getId()))
        .willReturn(Optional.of(servedOrder));

    // 실행
    Order newbie = orderService.startDelivery(servedOrder.getId());

    //검증
    assertThat(newbie.getStatus()).isEqualTo(OrderStatus.DELIVERING);
  }

  @DisplayName("주문 배달 완료 -> 성공")
  @Test
  void SHOULD_success_WHEN_complete_delivery_Order() {
    // 준비
    final Order deliveringOrder = new Order(DELIVERY_ORDER);
    deliveringOrder.setStatus(OrderStatus.DELIVERING);

    given(orderRepository.findById(deliveringOrder.getId()))
        .willReturn(Optional.of(deliveringOrder));

    // 실행
    Order newbie = orderService.completeDelivery(deliveringOrder.getId());

    //검증
    assertThat(newbie.getStatus()).isEqualTo(OrderStatus.DELIVERED);
  }

  static Stream<Arguments> orders() {
    final Order servedEatInOrder = new Order(EAT_IN_ORDER);
    servedEatInOrder.setStatus(OrderStatus.SERVED);

    final Order deliveredOrder = new Order(DELIVERY_ORDER);
    deliveredOrder.setStatus(OrderStatus.DELIVERED);

    return Stream.of(
        arguments(servedEatInOrder, "서빙 완료된 매장 주문"),
        arguments(deliveredOrder, "배달 완료된 배달 주문")
    );
  }

  @ParameterizedTest(name = "주문 완료 -> 성공 With {1}")
  @MethodSource("orders")
  void SHOULD_success_WHEN_complete_Order(Order order, String testDescription) {
    // 준비
    given(orderRepository.findById(order.getId()))
        .willReturn(Optional.of(order));
    given(orderRepository.existsByOrderTableAndStatusNot(ORDER_TABLE, OrderStatus.COMPLETED))
        .willReturn(false);

    // 실행
    Order newbie = orderService.complete(order.getId());

    //검증
    assertThat(newbie.getStatus()).isEqualTo(OrderStatus.COMPLETED);
  }

  @DisplayName("주문 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Orders() {
    // 준비
    given(orderRepository.findAll()).willReturn(ORDER_LIST);

    // 실행
    List<Order> orderList = orderService.findAll();

    // 검증
    assertThat(orderList).contains(EAT_IN_ORDER);
    assertThat(orderList).contains(DELIVERY_ORDER);
  }
}
