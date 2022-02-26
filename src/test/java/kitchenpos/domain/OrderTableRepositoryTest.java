package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderTableRepositoryTest {

  @Autowired private OrderTableRepository orderTableRepository;

  private OrderTable orderTable;

  @BeforeEach
  void setUp() {
    orderTable = new OrderTable(UUID.randomUUID(), "1번", 0, true);
  }

  @DisplayName("오더 테이블 저장 -> 성공")
  @Test
  void SHOULD_success_WHEN_save_Order_table() {
    OrderTable saved = orderTableRepository.save(orderTable);

    assertThat(saved).isEqualTo(orderTable);
  }

  @DisplayName("오더 테이블 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_find_by_id_Order_table() {
    OrderTable saved = orderTableRepository.save(orderTable);
    OrderTable found = orderTableRepository.findById(saved.getId()).orElse(null);

    assertThat(found).isEqualTo(saved);
  }

  @DisplayName("오더 테이블 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Order_tables() {
    OrderTable saved = orderTableRepository.save(orderTable);
    List<OrderTable> orderTableList = orderTableRepository.findAll();

    assertThat(orderTableList).contains(saved);
  }
}
