package kitchenpos.ui;

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
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeAll;
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

  private static Menu menu;
  private static MenuGroup menuGroup;
  private static MenuProduct menuProduct;
  private static Product product;


  @BeforeAll
  static void setUp() {
    product = new Product(UUID.randomUUID(), "싸이버거", BigDecimal.valueOf(5000));
    menuGroup = new MenuGroup(UUID.randomUUID(), "햄버거메뉴");
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

  @DisplayName("메뉴 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu() throws Exception {
    // 준비
    given(menuService.create(any())).willReturn(menu);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(menu.getId().toString()))
        .andExpect(jsonPath("name").value(menu.getName()))
        .andExpect(jsonPath("price").value(menu.getPrice()))
        .andExpect(jsonPath("$.menuGroup.id").value(menu.getMenuGroup().getId().toString()))
        .andExpect(jsonPath("displayed").value(menu.isDisplayed()))
        .andExpect(jsonPath("$.menuProducts[0].productId")
            .value(menu.getMenuProducts().get(0).getProductId().toString()));
  }

  @DisplayName("메뉴 숨기기 -> 성공")
  @Test
  void SHOULD_success_WHEN_hide_Menu() throws Exception {
    Menu clonedMenu = new Menu(menu);
    clonedMenu.setDisplayed(false);
    // 준비
    given(menuService.hide(any())).willReturn(clonedMenu);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/hide", clonedMenu.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clonedMenu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("displayed").value(false));
  }

  static Stream<Arguments> wrongMenus() {
    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
    Menu menuWithoutName = new Menu(menu);
    menuWithoutName.setName("");

    Menu menuWithoutPrice = new Menu(menu);
    menuWithoutPrice.setPrice(null);

    Menu menuWithNagtivePrice = new Menu(menu);
    menuWithNagtivePrice.setPrice(BigDecimal.valueOf(-1));

    Menu menuWithOverPrice = new Menu(menu);
    menuWithOverPrice.setPrice(BigDecimal.valueOf(Integer.MAX_VALUE));

    Menu menuWithZeroQuantity = new Menu(menu);
    menuWithZeroQuantity.getMenuProducts().get(0).setQuantity(0);

    Menu menuWithProfanity = new Menu(menu);
    menuWithProfanity.setName("예니");

//    BigDecimal overPrice = BigDecimal.valueOf(0);
//    for (final MenuProduct menuProduct : menu.getMenuProducts()) {
//      overPrice = overPrice.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
//    }

    return Stream.of(
        arguments(menuWithoutName, "빈 이름"),
        arguments(menuWithNagtivePrice, "가격 없음"),
        arguments(menuWithNagtivePrice, "음수 가격"),
        arguments(menuWithOverPrice, "창렬 가격"),
        arguments(menuWithZeroQuantity, "상품 갯수 0"),
        arguments(menuWithZeroQuantity, "욕설")
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
    Menu clonedMenu = new Menu(menu);
    clonedMenu.setDisplayed(true);
    // 준비
    given(menuService.display(any())).willReturn(clonedMenu);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/display", clonedMenu.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clonedMenu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("displayed").value(true));
  }

  @DisplayName("메뉴 가격 수정 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_price_of_Menu() throws Exception {
    Menu clonedMenu = new Menu(menu);

    final BigDecimal CHANGED_PRICE = BigDecimal.valueOf(8000);
    clonedMenu.setPrice(CHANGED_PRICE);
    // 준비
    given(menuService.changePrice(any(), any())).willReturn(clonedMenu);

    // 실행
    ResultActions perform = mockMvc.perform(
        put(String.format("/api/menus/%s/price", clonedMenu.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clonedMenu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("price").value(CHANGED_PRICE));
  }

  static Stream<Arguments> wrongPriceOfMenu() {
    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
    Menu menuWithoutPrice = new Menu(menu);
    menuWithoutPrice.setPrice(null);

    Menu menuWithNagtivePrice = new Menu(menu);
    menuWithNagtivePrice.setPrice(BigDecimal.valueOf(-1));

    Menu menuWithOverPrice = new Menu(menu);
    menuWithOverPrice.setPrice(BigDecimal.valueOf(Integer.MAX_VALUE));

    return Stream.of(
        arguments(menuWithNagtivePrice, "가격 없음"),
        arguments(menuWithNagtivePrice, "음수 가격"),
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
            .content(objectMapper.writeValueAsString(menu))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("메뉴 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menus() throws Exception {
    // 준비
    List<Menu> menuList = List.of(menu);

    given(menuService.findAll()).willReturn(menuList);

    // 실행
    ResultActions perform = mockMvc.perform(get("/api/menus")
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(menu.getId().toString()))
        .andExpect(jsonPath("$.[0].name").value(menu.getName()))
        .andExpect(jsonPath("$.[0].price").value(menu.getPrice()))
        .andExpect(jsonPath("$.[0].menuGroup.id").value(menu.getMenuGroup().getId().toString()))
        .andExpect(jsonPath("$.[0].displayed").value(menu.isDisplayed()))
        .andExpect(jsonPath("$.[0].menuProducts[0].productId")
            .value(menu.getMenuProducts().get(0).getProductId().toString()));
  }
}
