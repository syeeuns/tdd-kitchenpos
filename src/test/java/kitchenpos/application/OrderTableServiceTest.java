package kitchenpos.application;

import static kitchenpos.mocker.CoreMock.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.mocker.CoreMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableServiceTest {

  OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
  OrderRepository orderRepository = mock(OrderRepository.class);

  OrderTableService orderTableService = new OrderTableService(orderTableRepository, orderRepository);


  @DisplayName("오더 테이블 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Order_table() {
    // 준비
    given(orderTableRepository.save(any())).willReturn(ORDER_TABLE);

    // 실행
    OrderTable newbie = orderTableService.create(ORDER_TABLE);

    //검증
    assertThat(newbie).isEqualTo(ORDER_TABLE);
  }

//  @DisplayName("오더 테이블 생성 -> 실패")
//  @Test
//  void SHOULD_fail_WHEN_create_Order_table() {
//    // 준비
//    OrderTable orderTableWithoutName = CoreMock.copy(ORDER_TABLE);
//    orderTableWithoutName.setName(null);
//    given(orderTableRepository.save(any())).willThrow(IllegalArgumentException.class);
//
//    // 실행
//    assertThatThrownBy(() -> orderTableService.create(orderTableWithoutName))
//        .isInstanceOf(IllegalArgumentException.class);
//  }

  @DisplayName("오더 테이블 채우기 -> 성공")
  @Test
  void SHOULD_success_WHEN_sit_Order_table() {
    // 준비
    OrderTable clonedOrderTable = CoreMock.copy(ORDER_TABLE);
    clonedOrderTable.changeEmpty(false);
    given(orderTableRepository.fetchById(any())).willReturn(clonedOrderTable);

    // 실행
    OrderTable newbie = orderTableService.sit(clonedOrderTable.getId());

    //검증
    assertThat(newbie.isEmpty()).isEqualTo(false);
  }

  @DisplayName("오더 테이블 비우기 -> 성공")
  @Test
  void SHOULD_success_WHEN_clear_Order_table() {
    // 준비
    OrderTable clonedOrderTable = CoreMock.copy(ORDER_TABLE);
    clonedOrderTable.changeEmpty(true);
    given(orderTableRepository.fetchById(any())).willReturn(clonedOrderTable);

    // 실행
    OrderTable newbie = orderTableService.clear(clonedOrderTable.getId());

    //검증
    assertThat(newbie.isEmpty()).isEqualTo(true);
  }

  @DisplayName("오더 테이블 비우기 -> 실패")
  @Test
  void SHOULD_fail_WHEN_clear_Order_table() {
    // 준비
    given(orderTableRepository.findById(any())).willReturn(Optional.of(ORDER_TABLE));
    given(orderRepository.existsByOrderTableAndStatusNot(any(), any())).willThrow(IllegalStateException.class);

    // 실행
    assertThatThrownBy(() -> orderTableService.clear(ORDER_TABLE.getId()))
        .isInstanceOf(IllegalStateException.class);
  }

  @DisplayName("오더 테이블 인원 변경 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_number_of_guests_of_Order_table() {
    // 준비
    final int CHANGED_NUMBER_OF_GUESTS = 10;
    OrderTable clonedOrderTable = CoreMock.copy(ORDER_TABLE);
    clonedOrderTable.changeNumberOfGuests(CHANGED_NUMBER_OF_GUESTS);
    clonedOrderTable.changeEmpty(false);
    given(orderTableRepository.fetchById(any())).willReturn(clonedOrderTable);

    // 실행
    OrderTable newbie = orderTableService.changeNumberOfGuests(ORDER_TABLE.getId(), clonedOrderTable);

    //검증
    assertThat(newbie.getNumberOfGuests()).isEqualTo(CHANGED_NUMBER_OF_GUESTS);
  }

//  static Stream<Arguments> wrongOrderTables() {
//    OrderTable orderTableWithNegativeNumberOfGuests = CoreMock.copy(ORDER_TABLE);
//    orderTableWithNegativeNumberOfGuests.changeNumberOfGuests(-1);
//
//    OrderTable emptyOrderTable = CoreMock.copy(ORDER_TABLE);
//    emptyOrderTable.changeEmpty(true);
//
//    return Stream.of(
//        arguments(orderTableWithNegativeNumberOfGuests, "음수 손님"),
//        arguments(emptyOrderTable, "비어있는 테이블")
//    );
//  }
//
//  @ParameterizedTest(name = "오더 테이블 인원 변경 -> 실패 With {1}")
//  @MethodSource("wrongOrderTables")
//  void SHOULD_fail_WHEN_change_number_of_guests_of_Order_table(
//    OrderTable wrongOrderTable,
//    String testDescription
//  ) {
//    // 준비
//    given(orderTableRepository.findById(any())).willReturn(Optional.of(ORDER_TABLE));
//
//    // 실행
//    assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(ORDER_TABLE.getId(), wrongOrderTable))
//        .isInstanceOf(Exception.class);
//  }

  @DisplayName("오더 테이블 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_find_all_Order_table() {
    // 준비
    List<OrderTable> orderTableList = List.of(ORDER_TABLE);
    given(orderTableRepository.findAll()).willReturn(orderTableList);

    // 실행
    List<OrderTable> orderTables = orderTableRepository.findAll();

    //검증
    assertThat(orderTables).contains(ORDER_TABLE);
  }
}
