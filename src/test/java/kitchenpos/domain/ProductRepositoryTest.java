package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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

  @DisplayName("상품 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Product() {
    Product product2 = new Product(UUID.randomUUID(), "데리버거", BigDecimal.valueOf(1000));
    Product saved1 = productRepository.save(product);
    Product saved2 = productRepository.save(product2);

    List<Product> products = productRepository.findAll();

    assertThat(products).contains(saved1, saved2);
  }
}
