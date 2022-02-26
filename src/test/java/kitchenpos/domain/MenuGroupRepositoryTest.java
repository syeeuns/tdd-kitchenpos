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
class MenuGroupRepositoryTest {

  @Autowired private MenuGroupRepository menuGroupRepository;

  private MenuGroup menuGroup;

  @BeforeEach
  void setUp() {
    menuGroup = new MenuGroup(UUID.randomUUID(), "추천메뉴");
  }

  @DisplayName("메뉴 그룹 저장 -> 성공")
  @Test
  void SHOULD_success_WHEN_save_Menu_group() {
    MenuGroup saved = menuGroupRepository.save(menuGroup);

    assertThat(saved.getId()).isEqualTo(menuGroup.getId());
    assertThat(saved.getName()).isEqualTo(menuGroup.getName());
  }

  @DisplayName("메뉴 그룹 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menu_group() {
    MenuGroup menuGroup2 = new MenuGroup(UUID.randomUUID(), "신메뉴");
    MenuGroup saved1 = menuGroupRepository.save(menuGroup);
    MenuGroup saved2 = menuGroupRepository.save(menuGroup2);

    List<MenuGroup> menuGroups = menuGroupRepository.findAll();

    assertThat(menuGroups).contains(saved1, saved2);
  }
}
