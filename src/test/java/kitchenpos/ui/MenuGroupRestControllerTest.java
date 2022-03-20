package kitchenpos.ui;

import static kitchenpos.mocker.CoreMock.MENU_GROUP;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.mocker.CoreMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private MenuGroupService menuGroupService;


  @DisplayName("메뉴 그룹 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu_group() throws Exception {
    // 준비
    given(menuGroupService.create(any())).willReturn(MENU_GROUP);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(MENU_GROUP))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(MENU_GROUP.getId().toString()))
        .andExpect(jsonPath("name").value(MENU_GROUP.getName()));
  }

  @DisplayName("메뉴 그룹 생성 -> 실패")
  @Test
  void SHOULD_fail_WHEN_create_Menu_group() throws Exception {
    // 준비
    MenuGroup wrongMenuGroup = CoreMock.copy(MENU_GROUP);
    wrongMenuGroup.setName("");
    given(menuGroupService.create(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(wrongMenuGroup))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("메뉴 그룹 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menu_groups() throws Exception {
    // 준비
    MenuGroup menuGroup1 = CoreMock.copy(MENU_GROUP);
    MenuGroup menuGroup2 = CoreMock.copy(MENU_GROUP);
    menuGroup2.setName("신메뉴");
    List<MenuGroup> menuGroupList = List.of(menuGroup1, menuGroup2);

    given(menuGroupService.findAll()).willReturn(menuGroupList);

    // 실행
    ResultActions perform = mockMvc.perform(get("/api/menu-groups")
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(menuGroup1.getId().toString()))
        .andExpect(jsonPath("$.[0].name").value(menuGroup1.getName()))
        .andExpect(jsonPath("$.[1].id").value(menuGroup2.getId().toString()))
        .andExpect(jsonPath("$.[1].name").value(menuGroup2.getName()));
  }
}
