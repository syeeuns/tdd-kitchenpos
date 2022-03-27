package kitchenpos.domain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByIdIn(List<UUID> ids);

    default Product fetchById(UUID id) {
        return findById(id).orElseThrow(NoSuchElementException::new);
    }
}
