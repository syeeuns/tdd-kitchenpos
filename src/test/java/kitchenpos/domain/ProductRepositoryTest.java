package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;

  private Product product;

  @BeforeEach
  void setUp() {
    product = new Product(UUID.randomUUID(), "싸이버거", BigDecimal.valueOf(5000));
  }

  @DisplayName("상품 저장 -> 성공")
  @Test
  void SHOULD_success_WHEN_save_Product() {
    Product saved = productRepository.save(product);

    assertThat(saved.getId()).isEqualTo(product.getId());
    assertThat(saved.getName()).isEqualTo(product.getName());
    assertThat(saved.getPrice()).isEqualTo(product.getPrice());
  }
}
