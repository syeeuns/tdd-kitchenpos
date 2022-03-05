package kitchenpos.application;

import static kitchenpos.mocker.CoreMock.MENU_1;
import static kitchenpos.mocker.CoreMock.PRODUCT_1;
import static kitchenpos.mocker.CoreMock.PRODUCT_2;
import static kitchenpos.mocker.CoreMock.PRODUCT_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuServiceTest {

  ProductRepository productRepository = mock(ProductRepository.class);
  MenuRepository menuRepository = mock(MenuRepository.class);
  MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);

  PurgomalumClient purgomalumClient = mock(PurgomalumClient.class);
  MenuService menuService = new MenuService(
      menuRepository, menuGroupRepository, productRepository, purgomalumClient
  );

//  private static Menu menu;
//  private static MenuGroup menuGroup;
//  private static MenuProduct menuProduct;
//  private static Product product;


//  @BeforeAll
//  static void setUp() {
//    product = new Product(UUID.randomUUID(), "싸이버거", BigDecimal.valueOf(5000));
//    menuGroup = new MenuGroup(UUID.randomUUID(), "햄버거메뉴");
//    menuProduct = new MenuProduct(1L, product, 2L, product.getId());
//    menu = new Menu(UUID.randomUUID(),
//        "싸이버거 + 싸이버거",
//        BigDecimal.valueOf(9000),
//        menuGroup,
//        true,
//        List.of(menuProduct),
//        menuGroup.getId()
//    );
//  }

  @DisplayName("메뉴 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu() {
    // 준비
    given(menuRepository.save(any())).willReturn(MENU_1);
    given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(MENU_1.getMenuGroup()));
    given(productRepository.findById(PRODUCT_1.getId())).willReturn(Optional.of(PRODUCT_1));
    given(productRepository.findById(PRODUCT_2.getId())).willReturn(Optional.of(PRODUCT_2));
    given(productRepository.findAllById(any())).willReturn(PRODUCT_LIST);

    // 실행
    Menu newbie = menuService.create(MENU_1);

    //검증
    assertThat(newbie).isEqualTo(MENU_1);
  }

  static Stream<Arguments> wrongMenus() {
    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
    // TODO: changePrice에서 재활용할 수 있도록 생각해보기
    Menu menuWithEmptyName = new Menu(menu);
    menuWithEmptyName.setName("");

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
        arguments(menuWithEmptyName,  "빈 이름"),
        arguments(menuWithNagtivePrice, "가격 없음"),
        arguments(menuWithNagtivePrice, "음수 가격"),
        arguments(menuWithOverPrice, "창렬 가격"),
        arguments(menuWithZeroQuantity, "상품 갯수 0"),
        arguments(menuWithZeroQuantity, "욕설")
    );
  }

//  @ParameterizedTest(name = "메뉴 생성 -> 실패 With {1}")
//  @MethodSource("wrongMenus")
//  void SHOULD_fail_WHEN_create_Menu(Menu wrongMenu, String testDescription) throws Exception {
//    assertThatThrownBy(
//        // 실행
//        () -> menuService.create(wrongMenu)
//    // 검증
//    ).isInstanceOf(Exception.class);
//  }
//
//  @DisplayName("메뉴 가격 수정 -> 성공")
//  @Test
//  void SHOULD_success_WHEN_change_price_of_Menu() {
//    // 준비
//    final BigDecimal CHANGED_PRICE = menu.getPrice().add(BigDecimal.valueOf(1000));
//    Menu clonedMenu = new Menu(menu);
//    clonedMenu.setPrice(CHANGED_PRICE);
//
//    given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
//
//    // 실행
//    Menu newbie = menuService.changePrice(clonedMenu.getId(), clonedMenu);
//
//    //검증
//    assertThat(newbie).isEqualTo(clonedMenu);
//  }
//
//  static Stream<Arguments> menuListWithWrongPrice() {
//    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
//    Menu menuWithoutPrice = new Menu(menu);
//    menuWithoutPrice.setPrice(null);
//
//    Menu menuWithNagtivePrice = new Menu(menu);
//    menuWithNagtivePrice.setPrice(BigDecimal.valueOf(-1));
//
//    Menu menuWithOverPrice = new Menu(menu);
//    menuWithOverPrice.setPrice(BigDecimal.valueOf(Integer.MAX_VALUE));
//
//    return Stream.of(
//        arguments(menuWithNagtivePrice, "가격 없음"),
//        arguments(menuWithNagtivePrice, "음수 가격"),
//        arguments(menuWithOverPrice, "창렬 가격")
//    );
//  }
//
//  @ParameterizedTest(name = "메뉴 가격 수정 -> 실패 With {1}")
//  @MethodSource("menuListWithWrongPrice")
//  void SHOULD_fail_WHEN_change_price_of_Menu(Menu wrongMenu, String testDescription) throws Exception {
//    assertThatThrownBy(
//        // 실행
//        () -> menuService.changePrice(wrongMenu.getId(), wrongMenu)
//        // 검증
//    ).isInstanceOf(Exception.class);
//  }
//
//  @DisplayName("메뉴 보이기 -> 성공")
//  @Test
//  void SHOULD_success_WHEN_display_Menu() {
//    // 준비
//    given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
//
//    // 실행
//    Menu newbie = menuService.display(menu.getId());
//
//    //검증
//    assertThat(newbie.isDisplayed()).isEqualTo(true);
//  }
//
//  @DisplayName("메뉴 숨기기 -> 성공")
//  @Test
//  void SHOULD_success_WHEN_hide_Menu() {
//    // 준비
//    given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
//
//    // 실행
//    Menu newbie = menuService.hide(menu.getId());
//
//    //검증
//    assertThat(newbie.isDisplayed()).isEqualTo(false);
//  }
//
//  @DisplayName("메뉴 전체 조회 -> 성공")
//  @Test
//  void SHOULD_success_WHEN_findAll_Menus() {
//    // 준비
//    List<Menu> menuList = List.of(menu);
//
//    given(menuRepository.findAll()).willReturn(menuList);
//
//    // 실행
//    List<Menu> menus = menuService.findAll();
//
//    // 검증
//    assertThat(menus).contains(menu);
//  }
}
