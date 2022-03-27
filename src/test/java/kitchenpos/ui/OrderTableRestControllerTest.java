package kitchenpos.ui;

import static kitchenpos.mocker.CoreMock.ORDER_TABLE;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.application.OrderTableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.mocker.CoreMock;
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

@WebMvcTest(OrderTableRestController.class)
public class OrderTableRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private OrderTableService orderTableService;


  @DisplayName("오더 테이블 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Order_table() throws Exception {
    // 준비
    given(orderTableService.create(any())).willReturn(ORDER_TABLE);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/order-tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ORDER_TABLE))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(ORDER_TABLE.getId().toString()))
        .andExpect(jsonPath("name").value(ORDER_TABLE.getName()))
        .andExpect(jsonPath("numberOfGuests").value(ORDER_TABLE.getNumberOfGuests()))
        .andExpect(jsonPath("empty").value(ORDER_TABLE.isEmpty()));
  }

//  @DisplayName("오더 테이블 생성 -> 실패")
//  @Test
//  void SHOULD_fail_WHEN_create_Order_table() throws Exception {
//    // 준비
//    OrderTable orderTableWithoutName = CoreMock.copy(ORDER_TABLE);
//    orderTableWithoutName.changeName(null);
//    given(orderTableService.create(any())).willThrow(IllegalArgumentException.class);
//
//    // 실행
//    ResultActions perform = mockMvc.perform(
//        post("/api/order-tables")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(orderTableWithoutName))
//            .accept(MediaType.APPLICATION_JSON));
//
//    // 검증
//    perform.andExpect(status().is4xxClientError());
//  }

  @DisplayName("오더 테이블 채우기 -> 성공")
  @Test
  void SHOULD_success_WHEN_sit_Order_table() throws Exception {
    // 준비
    OrderTable clonedOrderTable = CoreMock.copy(ORDER_TABLE);
    clonedOrderTable.changeEmpty(false);
    given(orderTableService.sit(any())).willReturn(clonedOrderTable);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/order-tables/%s/sit", clonedOrderTable.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clonedOrderTable))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("empty").value(false));
  }

  @DisplayName("오더 테이블 비우기 -> 성공")
  @Test
  void SHOULD_success_WHEN_clear_Order_table() throws Exception {
    // 준비
    OrderTable clonedOrderTable = CoreMock.copy(ORDER_TABLE);
    clonedOrderTable.changeEmpty(true);
    given(orderTableService.clear(any())).willReturn(clonedOrderTable);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/order-tables/%s/clear", clonedOrderTable.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clonedOrderTable))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("empty").value(true));
  }

  @DisplayName("오더 테이블 인원 변경 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_number_of_guests_of_Order_table() throws Exception {
    // 준비
    final int CHANGED_NUMBER_OF_GUESTS = 10;
    OrderTable clonedOrderTable = CoreMock.copy(ORDER_TABLE);
    clonedOrderTable.changeNumberOfGuests(CHANGED_NUMBER_OF_GUESTS);
    clonedOrderTable.changeEmpty(false);
    given(orderTableService.changeNumberOfGuests(any(), any())).willReturn(clonedOrderTable);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/order-tables/%s/number-of-guests", clonedOrderTable.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clonedOrderTable))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("numberOfGuests").value(CHANGED_NUMBER_OF_GUESTS));
  }

  static Stream<Arguments> wrongOrderTables() {
//    OrderTable orderTableWithNegativeNumberOfGuests = CoreMock.copy(ORDER_TABLE);
//    orderTableWithNegativeNumberOfGuests.changeNumberOfGuests(-1);

    OrderTable emptyOrderTable = CoreMock.copy(ORDER_TABLE);
    emptyOrderTable.changeEmpty(true);

    return Stream.of(
//        arguments(orderTableWithNegativeNumberOfGuests, "음수 손님"),
        arguments(emptyOrderTable, "비어있는 테이블")
    );
  }

  @ParameterizedTest(name = "오더 테이블 인원 변경 -> 실패 With {1}")
  @MethodSource("wrongOrderTables")
  void SHOULD_fail_WHEN_change_number_of_guests_of_Order_table(
    OrderTable wrongOrderTable,
    String testDescription
  ) throws Exception {
    // 준비
    given(orderTableService.changeNumberOfGuests(any(), any()))
        .willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/order-tables/%s/number-of-guests", wrongOrderTable.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(wrongOrderTable))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("오더 테이블 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_find_all_Order_tables() throws Exception {
    // 준비
    List<OrderTable> orderTables = List.of(ORDER_TABLE);
    given(orderTableService.findAll()).willReturn(orderTables);

    // 실행
    ResultActions perform = mockMvc.perform(
        get("/api/order-tables")
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(ORDER_TABLE.getId().toString()))
        .andExpect(jsonPath("$.[0].name").value(ORDER_TABLE.getName()))
        .andExpect(jsonPath("$.[0].empty").value(ORDER_TABLE.isEmpty()))
        .andExpect(jsonPath("$.[0].numberOfGuests").value(ORDER_TABLE.getNumberOfGuests()));
  }
}
