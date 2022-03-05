package kitchenpos.ui;

import static kitchenpos.mocker.CoreMock.MAX_PRICE;
import static kitchenpos.mocker.CoreMock.MENU_1;
import static kitchenpos.mocker.CoreMock.MENU_2;
import static kitchenpos.mocker.CoreMock.MENU_LIST;
import static kitchenpos.mocker.CoreMock.NEGATIVE_PRICE;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private MenuService menuService;


  @DisplayName("메뉴 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu() throws Exception {
    // 준비
    given(menuService.create(any())).willReturn(MENU_1);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(MENU_1))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(MENU_1.getId().toString()))
        .andExpect(jsonPath("name").value(MENU_1.getName()))
        .andExpect(jsonPath("price").value(MENU_1.getPrice()))
        .andExpect(jsonPath("$.menuGroup.id").value(MENU_1.getMenuGroup().getId().toString()))
        .andExpect(jsonPath("displayed").value(MENU_1.isDisplayed()));
  }

  @DisplayName("메뉴 숨기기 -> 성공")
  @Test
  void SHOULD_success_WHEN_hide_Menu() throws Exception {
    Menu notDisplayedMenu = new Menu(MENU_1);
    notDisplayedMenu.setDisplayed(false);
    // 준비
    given(menuService.hide(any())).willReturn(notDisplayedMenu);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/hide", MENU_1.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(MENU_1))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("displayed").value(false));
  }

  static Stream<Arguments> wrongMenus() {
    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
    Menu menuWithoutName = new Menu(MENU_1);
    menuWithoutName.setName(null);

    Menu menuWithoutPrice = new Menu(MENU_1);
    menuWithoutPrice.setPrice(null);

    Menu menuWithNegativePrice = new Menu(MENU_1);
    menuWithNegativePrice.setPrice(NEGATIVE_PRICE);

    Menu menuWithOverPrice = new Menu(MENU_1);
    menuWithOverPrice.setPrice(MAX_PRICE);

    Menu menuWithZeroQuantity = new Menu(MENU_1);
    menuWithZeroQuantity.getMenuProducts().get(0).setQuantity(0);

    Menu menuWithProfanity = new Menu(MENU_1);
    menuWithProfanity.setName("예니");

    return Stream.of(
        arguments(menuWithoutName, "빈 이름"),
        arguments(menuWithNegativePrice, "가격 없음"),
        arguments(menuWithNegativePrice, "음수 가격"),
        arguments(menuWithOverPrice, "창렬 가격"),
        arguments(menuWithZeroQuantity, "상품 갯수 0"),
        arguments(menuWithProfanity, "욕설")
        );
  }

  @ParameterizedTest(name = "메뉴 생성 -> 실패 With {1}")
  @MethodSource("wrongMenus")
  void SHOULD_fail_WHEN_create_Menu(Menu wrongMenu, String testDescription) throws Exception {
    // 준비
    given(menuService.create(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(wrongMenu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("메뉴 보이기 -> 성공")
  @Test
  void SHOULD_success_WHEN_display_Menu() throws Exception {
    Menu notDisplayedMenu = new Menu(MENU_1);
    notDisplayedMenu.setDisplayed(false);
    // 준비
    given(menuService.display(any())).willReturn(MENU_1);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/display", notDisplayedMenu.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(MENU_1))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("displayed").value(true));
  }

  @DisplayName("메뉴 가격 수정 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_price_of_Menu() throws Exception {
    Menu priceChangedMenu = new Menu(MENU_1);

    priceChangedMenu.setPrice(BigDecimal.TEN);
    // 준비
    given(menuService.changePrice(any(), any())).willReturn(priceChangedMenu);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/price", MENU_1.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(MENU_1))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("price").value(BigDecimal.TEN));
  }

  static Stream<Arguments> wrongPriceOfMenu() {
    Menu menuWithoutPrice = new Menu(MENU_1);
    menuWithoutPrice.setPrice(null);

    Menu menuWithNegativePrice = new Menu(MENU_1);
    menuWithNegativePrice.setPrice(NEGATIVE_PRICE);

    Menu menuWithOverPrice = new Menu(MENU_1);
    menuWithOverPrice.setPrice(MAX_PRICE);

    return Stream.of(
        arguments(menuWithNegativePrice, "가격 없음"),
        arguments(menuWithNegativePrice, "음수 가격"),
        arguments(menuWithOverPrice, "창렬 가격")
    );
  }

  @ParameterizedTest(name = "메뉴 생성 -> 실패 With {1}")
  @MethodSource("wrongPriceOfMenu")
  void SHOULD_fail_WHEN_change_price_of_Menu(Menu wrongMenu, String testDescription) throws Exception {
    // 준비
    given(menuService.changePrice(any(), any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/price", wrongMenu.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(wrongMenu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("메뉴 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menus() throws Exception {
    // 준비
    given(menuService.findAll()).willReturn(MENU_LIST);

    // 실행
    ResultActions perform = mockMvc.perform(get("/api/menus")
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(MENU_1.getId().toString()))
        .andExpect(jsonPath("$.[0].name").value(MENU_1.getName()))
        .andExpect(jsonPath("$.[0].price").value(MENU_1.getPrice()))
        .andExpect(jsonPath("$.[0].menuGroup.id").value(MENU_1.getMenuGroup().getId().toString()))
        .andExpect(jsonPath("$.[0].displayed").value(MENU_1.isDisplayed()))
        .andExpect(jsonPath("$.[1].id").value(MENU_2.getId().toString()))
        .andExpect(jsonPath("$.[1].name").value(MENU_2.getName()))
        .andExpect(jsonPath("$.[1].price").value(MENU_2.getPrice()))
        .andExpect(jsonPath("$.[1].menuGroup.id").value(MENU_2.getMenuGroup().getId().toString()))
        .andExpect(jsonPath("$.[1].displayed").value(MENU_2.isDisplayed()));
  }
}
