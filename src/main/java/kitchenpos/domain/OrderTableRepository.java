package kitchenpos.domain;

import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, UUID> {
  default OrderTable fetchById(UUID id) {
    return findById(id).orElseThrow(NoSuchElementException::new);
  }
}
