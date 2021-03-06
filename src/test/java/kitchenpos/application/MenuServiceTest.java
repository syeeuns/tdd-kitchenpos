package kitchenpos.application;

import static kitchenpos.mocker.CoreMock.MENU_1;
import static kitchenpos.mocker.CoreMock.MENU_2;
import static kitchenpos.mocker.CoreMock.MENU_LIST;
import static kitchenpos.mocker.CoreMock.PRODUCT_1;
import static kitchenpos.mocker.CoreMock.PRODUCT_2;
import static kitchenpos.mocker.CoreMock.PRODUCT_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import kitchenpos.mocker.CoreMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest {

  ProductRepository productRepository = mock(ProductRepository.class);
  MenuRepository menuRepository = mock(MenuRepository.class);
  MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);

  PurgomalumClient purgomalumClient = mock(PurgomalumClient.class);
  MenuService menuService = new MenuService(
      menuRepository, menuGroupRepository, productRepository, purgomalumClient
  );


  @DisplayName("메뉴 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Menu() {
    // 준비
    given(menuRepository.save(any())).willReturn(MENU_1);
    given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(MENU_1.getMenuGroup()));
    given(productRepository.findAllByIdIn(any())).willReturn(PRODUCT_LIST);
    given(productRepository.findById(PRODUCT_1.getId())).willReturn(Optional.of(PRODUCT_1));
    given(productRepository.findById(PRODUCT_2.getId())).willReturn(Optional.of(PRODUCT_2));
    given(productRepository.findAllById(any())).willReturn(PRODUCT_LIST);

    // 실행
    Menu newbie = menuService.create(MENU_1);

    //검증
    assertThat(newbie).isEqualTo(MENU_1);
  }

  // TODO: Validate 로직 만들면 주석까지 지우기
//  static Stream<Arguments> wrongMenus() {
//    Menu menuWithEmptyName = CoreMock.copy(MENU_1);
//    menuWithEmptyName.setName("");
//
//    Menu menuWithoutPrice = CoreMock.copy(MENU_1);
//    menuWithoutPrice.changePrice(null);
//
//    Menu menuWithNagtivePrice = CoreMock.copy(MENU_1);
//    menuWithNagtivePrice.changePrice(NEGATIVE_PRICE);
//
//    Menu menuWithOverPrice = CoreMock.copy(MENU_1);
//    menuWithOverPrice.changePrice(MAX_PRICE);
//
//    Menu menuWithZeroQuantity = CoreMock.copy(MENU_1);
//    MenuProduct copiedMenuProduct1 = CoreMock.copy(CoreMock.MENU_PRODUCT_1);
//    copiedMenuProduct1.setQuantity(0);
//    menuWithZeroQuantity.setMenuProducts(List.of(copiedMenuProduct1, copiedMenuProduct1));
//
//    Menu menuWithProfanity = CoreMock.copy(MENU_1);
//    menuWithProfanity.setName("예니");
//
//    return Stream.of(
//        arguments(menuWithEmptyName,  "빈 이름"),
//        arguments(menuWithNagtivePrice, "가격 없음"),
//        arguments(menuWithNagtivePrice, "음수 가격"),
//        arguments(menuWithOverPrice, "창렬 가격"),
//        arguments(menuWithZeroQuantity, "상품 갯수 0"),
//        arguments(menuWithZeroQuantity, "욕설")
//    );
//  }
//
//  @ParameterizedTest(name = "메뉴 생성 -> 실패 With {1}")
//  @MethodSource("wrongMenus")
//  void SHOULD_fail_WHEN_create_Menu(Menu wrongMenu, String testDescription) throws Exception {
//    assertThatThrownBy(
//        // 실행
//        () -> menuService.create(wrongMenu)
//    // 검증
//    ).isInstanceOf(Exception.class);
//  }

  @DisplayName("메뉴 가격 수정 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_price_of_Menu() {
    // 준비
    Menu priceChangedMenu = CoreMock.copy(MENU_1);
    priceChangedMenu.changePrice(BigDecimal.TEN);

    given(menuRepository.findById(any())).willReturn(Optional.ofNullable(priceChangedMenu));

    // 실행
    Menu newbie = menuService.changePrice(priceChangedMenu.getId(), priceChangedMenu);

    //검증
    assertThat(newbie).isEqualTo(priceChangedMenu);
  }

  // TODO: Validate 로직 만들면 주석까지 지우기
//  static Stream<Arguments> menuListWithWrongPrice() {
//    Menu menuWithoutPrice = CoreMock.copy(MENU_1);
//    menuWithoutPrice.changePrice(null);
//
//    Menu menuWithNegativePrice = CoreMock.copy(MENU_1);
//    menuWithNegativePrice.changePrice(NEGATIVE_PRICE);
//
//    Menu menuWithOverPrice = CoreMock.copy(MENU_1);
//    menuWithOverPrice.changePrice(MAX_PRICE);
//
//    return Stream.of(
//        arguments(menuWithoutPrice, "가격 없음"),
//        arguments(menuWithNegativePrice, "음수 가격"),
//        arguments(menuWithOverPrice, "창렬 가격")
//    );
//  }

//  @ParameterizedTest(name = "메뉴 가격 수정 -> 실패 With {1}")
//  @MethodSource("menuListWithWrongPrice")
//  void SHOULD_fail_WHEN_change_price_of_Menu(Menu wrongMenu, String testDescription) throws Exception {
//    assertThatThrownBy(
//        // 실행
//        () -> menuService.changePrice(wrongMenu.getId(), wrongMenu)
//        // 검증
//    ).isInstanceOf(Exception.class);
//  }

  // TODO: 로직이 이상한 것 같아 더블체크 후 수정 (MenuService.display() sum 계산 부분)
  @DisplayName("메뉴 보이기 -> 성공")
  @Test
  void SHOULD_success_WHEN_display_Menu() {
    // 준비
    Menu notDisplayedMenu = CoreMock.copy(MENU_1);
    notDisplayedMenu.changeDisplayed(false);
    given(menuRepository.findById(any())).willReturn(Optional.of(notDisplayedMenu));

    // 실행
    Menu newbie = menuService.display(notDisplayedMenu.getId());

    //검증
    assertThat(newbie.isDisplayed()).isEqualTo(true);
  }

  @DisplayName("메뉴 숨기기 -> 성공")
  @Test
  void SHOULD_success_WHEN_hide_Menu() {
    // 준비
    Menu notDisplayedMenu = CoreMock.copy(MENU_1);
    notDisplayedMenu.changeDisplayed(false);
    given(menuRepository.findById(any())).willReturn(Optional.of(notDisplayedMenu));

    // 실행
    Menu newbie = menuService.hide(notDisplayedMenu.getId());

    //검증
    assertThat(newbie.isDisplayed()).isEqualTo(false);
  }

  @DisplayName("메뉴 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Menus() {
    // 준비
    given(menuRepository.findAll()).willReturn(MENU_LIST);

    // 실행
    List<Menu> menus = menuService.findAll();

    // 검증
    assertThat(menus).contains(MENU_1);
    assertThat(menus).contains(MENU_2);
  }
}
