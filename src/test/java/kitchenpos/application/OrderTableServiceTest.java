package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTableServiceTest {

  OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
  OrderRepository orderRepository = mock(OrderRepository.class);

  OrderTableService orderTableService = new OrderTableService(orderTableRepository, orderRepository);

  private static OrderTable orderTable;


  @BeforeAll
  static void setUp() {
    orderTable = new OrderTable(UUID.randomUUID(), "1번", 0, true);
  }

  @DisplayName("오더 테이블 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Order_table() {
    // 준비
    given(orderTableRepository.save(any())).willReturn(orderTable);

    // 실행
    OrderTable newbie = orderTableService.create(orderTable);

    //검증
    assertThat(newbie).isEqualTo(orderTable);
  }

  @DisplayName("오더 테이블 생성 -> 실패")
  @Test
  void SHOULD_fail_WHEN_create_Order_table() {
    // 준비
    OrderTable orderTableWithoutName = new OrderTable(orderTable);
    orderTableWithoutName.setName(null);
    given(orderTableRepository.save(any())).willThrow(IllegalArgumentException.class);

    // 실행
    assertThatThrownBy(() -> orderTableService.create(orderTableWithoutName))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("오더 테이블 채우기 -> 성공")
  @Test
  void SHOULD_success_WHEN_sit_Order_table() {
    // 준비
    OrderTable clonedOrderTable = new OrderTable(orderTable);
    clonedOrderTable.setEmpty(false);
    given(orderTableRepository.findById(any())).willReturn(Optional.of(clonedOrderTable));

    // 실행
    OrderTable newbie = orderTableService.sit(clonedOrderTable.getId());

    //검증
    assertThat(newbie.isEmpty()).isEqualTo(false);
  }

  @DisplayName("오더 테이블 비우기 -> 성공")
  @Test
  void SHOULD_success_WHEN_clear_Order_table() {
    // 준비
    OrderTable clonedOrderTable = new OrderTable(orderTable);
    clonedOrderTable.setEmpty(true);
    given(orderTableRepository.findById(any())).willReturn(Optional.of(clonedOrderTable));

    // 실행
    OrderTable newbie = orderTableService.clear(clonedOrderTable.getId());

    //검증
    assertThat(newbie.isEmpty()).isEqualTo(true);
  }

  @DisplayName("오더 테이블 비우기 -> 실패")
  @Test
  void SHOULD_fail_WHEN_clear_Order_table() {
    // 준비
    given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
    given(orderRepository.existsByOrderTableAndStatusNot(any(), any())).willThrow(IllegalStateException.class);

    // 실행
    assertThatThrownBy(() -> orderTableService.clear(orderTable.getId()))
        .isInstanceOf(IllegalStateException.class);
  }

  @DisplayName("오더 테이블 인원 변경 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_number_of_guests_of_Order_table() {
    // 준비
    final int CHANGED_NUMBER_OF_GUESTS = 10;
    OrderTable clonedOrderTable = new OrderTable(orderTable);
    clonedOrderTable.setNumberOfGuests(CHANGED_NUMBER_OF_GUESTS);
    clonedOrderTable.setEmpty(false);
    given(orderTableRepository.findById(any())).willReturn(Optional.of(clonedOrderTable));

    // 실행
    OrderTable newbie = orderTableService.changeNumberOfGuests(orderTable.getId(), clonedOrderTable);

    //검증
    assertThat(newbie.getNumberOfGuests()).isEqualTo(CHANGED_NUMBER_OF_GUESTS);
  }

  static Stream<Arguments> wrongOrderTables() {
    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
    OrderTable orderTableWithNegativeNumberOfGuests = new OrderTable(orderTable);
    orderTableWithNegativeNumberOfGuests.setNumberOfGuests(-1);

    OrderTable emptyOrderTable = new OrderTable(orderTable);
    emptyOrderTable.setEmpty(true);

    return Stream.of(
        arguments(orderTableWithNegativeNumberOfGuests, "음수 손님"),
        arguments(emptyOrderTable, "비어있는 테이블")
    );
  }

  @ParameterizedTest(name = "오더 테이블 인원 변경 -> 실패 With {1}")
  @MethodSource("wrongOrderTables")
  void SHOULD_fail_WHEN_change_number_of_guests_of_Order_table(OrderTable wrongOrderTable, String testDescription) {
    // 준비
    given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

    // 실행
    assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), wrongOrderTable))
        .isInstanceOf(Exception.class);
  }

  @DisplayName("오더 테이블 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_find_all_Order_table() {
    // 준비
    List<OrderTable> orderTableList = List.of(orderTable);
    given(orderTableRepository.findAll()).willReturn(orderTableList);

    // 실행
    List<OrderTable> orderTables = orderTableRepository.findAll();

    //검증
    assertThat(orderTables).contains(orderTable);
  }
//
//  static Stream<Arguments> wrongMenus() {
//    // TODO: 생성자가 아니라 Builder 패턴으로 만들기
//    // TODO: changePrice에서 재활용할 수 있도록 생각해보기
//    Menu menuWithEmptyName = new Menu(menu);
//    menuWithEmptyName.setName("");
//
//    Menu menuWithoutPrice = new Menu(menu);
//    menuWithoutPrice.setPrice(null);
//
//    Menu menuWithNagtivePrice = new Menu(menu);
//    menuWithNagtivePrice.setPrice(BigDecimal.valueOf(-1));
//
//    Menu menuWithOverPrice = new Menu(menu);
//    menuWithOverPrice.setPrice(BigDecimal.valueOf(Integer.MAX_VALUE));
//
//    Menu menuWithZeroQuantity = new Menu(menu);
//    menuWithZeroQuantity.getMenuProducts().get(0).setQuantity(0);
//
//    Menu menuWithProfanity = new Menu(menu);
//    menuWithProfanity.setName("예니");
//
////    BigDecimal overPrice = BigDecimal.valueOf(0);
////    for (final MenuProduct menuProduct : menu.getMenuProducts()) {
////      overPrice = overPrice.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
////    }
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
