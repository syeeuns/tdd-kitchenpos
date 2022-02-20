package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.UUID;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

  MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);
  MenuGroupService menuGroupService = new MenuGroupService(menuGroupRepository);

  private MenuGroup menuGroup;


  @BeforeEach
  void setUp() {
    menuGroup = new MenuGroup(UUID.randomUUID(), "추천메뉴");
  }

  @DisplayName("메뉴 그룹 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu_group() {
    // 준비
    given(menuGroupRepository.save(any())).willReturn(menuGroup);

    // 실행
    MenuGroup newbie = menuGroupService.create(menuGroup);

    //검증
    assertThat(newbie.getId()).isEqualTo(menuGroup.getId());
    assertThat(newbie.getName()).isEqualTo(menuGroup.getName());
  }

  @DisplayName("메뉴 그룹 생성 -> 실패")
  @Test
  void SHOULD_fail_WHEN_create_Menu_group() {
    // 준비
    MenuGroup menuGroupWithWrongName = new MenuGroup(UUID.randomUUID(), "");
    given(menuGroupRepository.save(any())).willThrow(IllegalArgumentException.class);

    // 실행
    assertThatThrownBy(() -> menuGroupService.create(menuGroupWithWrongName))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 그룹 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menu_groups() {
    // 준비
    MenuGroup menuGroup2 = new MenuGroup(UUID.randomUUID(), "신메뉴");
    List<MenuGroup> menuGroupList = List.of(menuGroup, menuGroup2);

    given(menuGroupRepository.findAll()).willReturn(menuGroupList);

    // 실행
    // 실행
    List<MenuGroup> menuGroups = menuGroupRepository.findAll();

    // 검증
    assertThat(menuGroups.get(0).getId()).isEqualTo(menuGroupList.get(0).getId());
    assertThat(menuGroups.get(0).getName()).isEqualTo(menuGroupList.get(0).getName());
    assertThat(menuGroups.get(1).getId()).isEqualTo(menuGroupList.get(1).getId());
    assertThat(menuGroups.get(1).getName()).isEqualTo(menuGroupList.get(1).getName());
  }
}
