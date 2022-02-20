package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
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

  private MenuGroup menuGroup;


  @BeforeEach
  void setUp() {
    menuGroup = new MenuGroup(UUID.randomUUID(), "추천메뉴");
  }

  @DisplayName("메뉴 그룹 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu_group() throws Exception {
    // 준비
    given(menuGroupService.create(any())).willReturn(menuGroup);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuGroup))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(menuGroup.getId().toString()))
        .andExpect(jsonPath("name").value(menuGroup.getName()));
  }

  @DisplayName("메뉴 그룹 생성 -> 실패")
  @Test
  void SHOULD_fail_WHEN_create_Menu_group() throws Exception {
    // 준비
    MenuGroup wrongMenuGroup = new MenuGroup(UUID.randomUUID(), "");
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
    MenuGroup menuGroup2 = new MenuGroup(UUID.randomUUID(), "신메뉴");
    List<MenuGroup> menuGroupList = List.of(menuGroup, menuGroup2);

    given(menuGroupService.findAll()).willReturn(menuGroupList);

    // 실행
    ResultActions perform = mockMvc.perform(get("/api/menu-groups")
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(menuGroup.getId().toString()))
        .andExpect(jsonPath("$.[0].name").value(menuGroup.getName()))
        .andExpect(jsonPath("$.[1].id").value(menuGroup2.getId().toString()))
        .andExpect(jsonPath("$.[1].name").value(menuGroup2.getName()));
  }
}
