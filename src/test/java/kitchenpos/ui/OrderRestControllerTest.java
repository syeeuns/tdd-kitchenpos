package kitchenpos.ui;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {

  public static final String DELIVERY_ADDRESS = "서울특별시 삼성동 512";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private OrderService orderService;

  private static Order basicOrder;
  private static Order deliveryOrder;
  private static Order eatInOrder;
  private static Order takeoutOrder;

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
  void SHOULD_success_WHEN_create_Order() throws Exception {
    // 준비
    given(orderService.create(any())).willReturn(eatInOrder);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eatInOrder))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated());
  }

  static Stream<Arguments> wrongOrdersForCreate() {
    Order orderWithoutType = new Order(basicOrder);
    orderWithoutType.setType(null);

    Order deliveryOrderWithoutAddress = new Order(deliveryOrder);
    deliveryOrderWithoutAddress.setDeliveryAddress(null);

    return Stream.of(
        arguments(orderWithoutType, "타입없는 주문"),
        arguments(deliveryOrderWithoutAddress, "주소없는 배달 주문")
    );
  }

  @ParameterizedTest(name = "주문 생성 -> 실패 With {1}")
  @MethodSource("wrongOrdersForCreate")
  void SHOULD_fail_WHEN_create_Order(Order wrongOrder, String testDescription) throws Exception {
    // 준비
    given(orderService.create(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(wrongOrder))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("주문 접수 -> 성공")
  @Test
  void SHOULD_success_WHEN_accept_Order() throws Exception {
    // 준비
    Order waitingEatInOrder = new Order(eatInOrder);
    waitingEatInOrder.setStatus(OrderStatus.WAITING);

    Order acceptedEatInOrder = new Order(eatInOrder);
    acceptedEatInOrder.setStatus(OrderStatus.ACCEPTED);

    given(orderService.accept(any())).willReturn(acceptedEatInOrder);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/accept", waitingEatInOrder.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("id").value(acceptedEatInOrder.getId().toString()))
        .andExpect(jsonPath("status").value(acceptedEatInOrder.getStatus().toString()));
  }

  @DisplayName("주문 접수 -> 실패")
  @Test
  void SHOULD_fail_WHEN_accept_Order() throws Exception {
    // 준비
    Order waitingEatInOrder = new Order(eatInOrder);
    waitingEatInOrder.setStatus(OrderStatus.SERVED);

    given(orderService.accept(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/accept", waitingEatInOrder.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("주문 서빙 -> 성공")
  @Test
  void SHOULD_success_WHEN_serve_Order() throws Exception {
    // 준비
    Order acceptedEatInOrder = new Order(eatInOrder);
    acceptedEatInOrder.setStatus(OrderStatus.ACCEPTED);

    Order servedEatInOrder = new Order(eatInOrder);
    servedEatInOrder.setStatus(OrderStatus.SERVED);

    given(orderService.accept(any())).willReturn(servedEatInOrder);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/accept", acceptedEatInOrder.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("id").value(servedEatInOrder.getId().toString()))
        .andExpect(jsonPath("status").value(servedEatInOrder.getStatus().toString()));
  }

  @DisplayName("주문 서빙 -> 실패")
  @Test
  void SHOULD_fail_WHEN_serve_Order() throws Exception {
    // 준비
    Order notAcceptedEatInOrder = new Order(eatInOrder);
    notAcceptedEatInOrder.setStatus(OrderStatus.SERVED);

    given(orderService.serve(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/serve", notAcceptedEatInOrder.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("주문 배달 시작 -> 성공")
  @Test
  void SHOULD_success_WHEN_start_delivery_Order() throws Exception {
    // 준비
    Order servedDeliveryOrder = new Order(deliveryOrder);
    servedDeliveryOrder.setStatus(OrderStatus.SERVED);

    Order deliveringOrder = new Order(deliveryOrder);
    deliveringOrder.setStatus(OrderStatus.DELIVERING);

    given(orderService.startDelivery(any())).willReturn(deliveringOrder);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/start-delivery", servedDeliveryOrder.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("id").value(deliveringOrder.getId().toString()))
        .andExpect(jsonPath("status").value(deliveringOrder.getStatus().toString()));
  }

  static Stream<Arguments> wrongOrdersForDelivery() {
    Order notServedDeliveryOrder = new Order(deliveryOrder);
    notServedDeliveryOrder.setStatus(OrderStatus.WAITING);

    return Stream.of(
        arguments(eatInOrder, "타입이 올바르지 않은 주문"),
        arguments(notServedDeliveryOrder, "상태가 올바르지 않은 주문")
    );
  }

  @ParameterizedTest(name = "주문 배달 시작 -> 실패 With {1}")
  @MethodSource("wrongOrdersForDelivery")
  void SHOULD_fail_WHEN_start_delivery_Order(Order wrongOrder, String testDescription)
      throws Exception {
    // 준비
    given(orderService.startDelivery(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/start-delivery", wrongOrder.getId()))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("주문 배달 완료 -> 성공")
  @Test
  void SHOULD_success_WHEN_complete_delivery_Order() throws Exception {
    // 준비
    Order deliveringOrder = new Order(deliveryOrder);
    deliveringOrder.setStatus(OrderStatus.DELIVERING);

    Order completedDeliveryOrder = new Order(deliveryOrder);
    completedDeliveryOrder.setStatus(OrderStatus.DELIVERED);

    given(orderService.completeDelivery(any())).willReturn(completedDeliveryOrder);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/complete-delivery", deliveringOrder.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("id").value(completedDeliveryOrder.getId().toString()))
        .andExpect(jsonPath("status").value(completedDeliveryOrder.getStatus().toString()));
  }

  @ParameterizedTest(name = "주문 배달 시작 -> 실패 With {1}")
  @MethodSource("wrongOrdersForDelivery")
  void SHOULD_fail_WHEN_complete_delivery_Order(Order wrongOrder, String testDescription)
      throws Exception {
    // 준비
    given(orderService.completeDelivery(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/complete-delivery", wrongOrder.getId()))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  static Stream<Arguments> ordersForComplete() {
    Order servedEatInOrder = new Order(eatInOrder);
    servedEatInOrder.setStatus(OrderStatus.SERVED);

    Order completedDeliveryOrder = new Order(deliveryOrder);
    completedDeliveryOrder.setStatus(OrderStatus.DELIVERED);
    return Stream.of(
        arguments(servedEatInOrder, "서빙나간 주문"),
        arguments(completedDeliveryOrder, "배달된 주문")
    );
  }

  @ParameterizedTest(name = "주문 완료 -> 성공 With {1}")
  @MethodSource("ordersForComplete")
  void SHOULD_success_WHEN_complete_Order(Order order, String testDescription) throws Exception {
    // 준비
    Order completedOrder = new Order(order);
    completedOrder.setStatus(OrderStatus.COMPLETED);

    given(orderService.complete(any())).willReturn(completedOrder);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/complete", order.getId()))
            .accept(MediaType.APPLICATION_JSON)
    );

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("id").value(completedOrder.getId().toString()))
        .andExpect(jsonPath("status").value(completedOrder.getStatus().toString()));
  }

  static Stream<Arguments> wrongOrdersForComplete() {
    Order notDeliveredDeliveryOrder = new Order(deliveryOrder);
    notDeliveredDeliveryOrder.setStatus(OrderStatus.WAITING);

    Order eatInOrderNotServed = new Order(eatInOrder);
    eatInOrderNotServed.setStatus(OrderStatus.WAITING);

    return Stream.of(
        arguments(notDeliveredDeliveryOrder, "상태가 DELIVERED가 아닌 배달 주문"),
        arguments(eatInOrderNotServed, "상태가 SERVED가 아닌 매장 주문")
    );
  }

  @ParameterizedTest(name = "주문 완료 -> 실패 With {1}")
  @MethodSource("wrongOrdersForComplete")
  void SHOULD_fail_WHEN_complete_Order(Order wrongOrder, String testDescription)
      throws Exception {
    // 준비
    given(orderService.complete(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/orders/%s/complete", wrongOrder.getId()))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("주문 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Orders() throws Exception {
    // 준비
    Order clonedDeliveryOrder = new Order(deliveryOrder);
    clonedDeliveryOrder.setStatus(OrderStatus.ACCEPTED);
    List<Order> orderList = List.of(clonedDeliveryOrder);

    given(orderService.findAll()).willReturn(orderList);

    // 실행
    ResultActions perform = mockMvc.perform(get("/api/orders")
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(clonedDeliveryOrder.getId().toString()))
        .andExpect(jsonPath("$.[0].type").value(clonedDeliveryOrder.getType().toString()))
        .andExpect(jsonPath("$.[0].status").value(clonedDeliveryOrder.getStatus().toString()))
        .andExpect(jsonPath("$.[0].deliveryAddress").value(deliveryOrder.getDeliveryAddress()));
  }
}
