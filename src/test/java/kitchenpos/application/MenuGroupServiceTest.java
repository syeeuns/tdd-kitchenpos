package kitchenpos.application;

import static kitchenpos.mocker.CoreMock.MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.mocker.CoreMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

  MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);
  MenuGroupService menuGroupService = new MenuGroupService(menuGroupRepository);


  @DisplayName("메뉴 그룹 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu_group() {
    // 준비
    given(menuGroupRepository.save(any())).willReturn(MENU_GROUP);

    // 실행
    MenuGroup newbie = menuGroupService.create(MENU_GROUP);

    //검증
    assertThat(newbie.getId()).isEqualTo(MENU_GROUP.getId());
    assertThat(newbie.getName()).isEqualTo(MENU_GROUP.getName());
  }

  @DisplayName("메뉴 그룹 생성 -> 실패")
  @Test
  void SHOULD_fail_WHEN_create_Menu_group() {
    // 준비
    MenuGroup menuGroupWithWrongName = CoreMock.copy(MENU_GROUP);
    menuGroupWithWrongName.setName("");
    given(menuGroupRepository.save(any())).willThrow(IllegalArgumentException.class);

    // 실행
    assertThatThrownBy(() -> menuGroupService.create(menuGroupWithWrongName))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 그룹 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menu_groups() {
    // 준비
    MenuGroup menuGroup1 = CoreMock.copy(MENU_GROUP);
    MenuGroup menuGroup2 = CoreMock.copy(MENU_GROUP);
    menuGroup2.setName("신메뉴");
    List<MenuGroup> menuGroupList = List.of(menuGroup1, menuGroup2);

    given(menuGroupRepository.findAll()).willReturn(menuGroupList);

    // 실행
    // 실행
    List<MenuGroup> menuGroups = menuGroupRepository.findAll();

    // 검증
    assertThat(menuGroups).contains(menuGroup1, menuGroup2);
  }
}
