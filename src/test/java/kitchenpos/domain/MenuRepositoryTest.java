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
class MenuRepositoryTest {

  @Autowired private MenuRepository menuRepository;
  @Autowired private MenuGroupRepository menuGroupRepository;
  @Autowired private ProductRepository productRepository;

  private static Menu menu;
  private static MenuGroup menuGroup;
  private static MenuProduct menuProduct;
  private static Product product;

  @BeforeEach
  void setUp() {
    product = new Product(UUID.randomUUID(), "싸이버거", BigDecimal.valueOf(5000));
    productRepository.save(product);

    menuGroup = new MenuGroup(UUID.randomUUID(), "햄버거메뉴");
    menuGroupRepository.save(menuGroup);

    menuProduct = new MenuProduct(1L, product, 2L, product.getId());
    menu = new Menu(UUID.randomUUID(),
        "싸이버거 + 싸이버거",
        BigDecimal.valueOf(9000),
        menuGroup,
        true,
        List.of(menuProduct),
        menuGroup.getId()
    );
  }

  @DisplayName("메뉴 저장 -> 성공")
  @Test
  void SHOULD_success_WHEN_save_Menu() {
    Menu saved = menuRepository.save(menu);

    assertThat(saved).isEqualTo(menu);
  }

  @DisplayName("메뉴 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Product() {
    Menu saved = menuRepository.save(menu);
    List<Menu> menuList = menuRepository.findAll();

    assertThat(menuList).contains(saved);
  }
}
