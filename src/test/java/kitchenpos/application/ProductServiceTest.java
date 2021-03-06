package kitchenpos.application;

import static kitchenpos.mocker.CoreMock.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import kitchenpos.mocker.CoreMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

  ProductRepository productRepository = mock(ProductRepository.class);
  MenuRepository menuRepository = mock(MenuRepository.class);
  PurgomalumClient purgomalumClient = mock(PurgomalumClient.class);
  ProductService productService = new ProductService(productRepository, menuRepository, purgomalumClient);


  @DisplayName("상품 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Product() {
    // 준비
    given(productRepository.save(any())).willReturn(PRODUCT_1);

    // 실행
    Product newbie = productService.create(PRODUCT_1);

    //검증
    assertThat(newbie).isEqualTo(PRODUCT_1);
  }

  // TODO: Validate 로직 만들면 주석까지 지우기
//  static Stream<Arguments> wrongProducts1() {
//    Product nullNameProduct = CoreMock.copy(PRODUCT_1);
//    nullNameProduct.setName(null);
//
//    Product negativePriceProduct = CoreMock.copy(PRODUCT_1);
//    negativePriceProduct.setPrice(BigDecimal.valueOf(-1));
//
//    return Stream.of(
//        arguments(nullNameProduct),
//        arguments(
//            new Product.Builder()
//                .id(UUID.randomUUID())
//                .name("데리버거")
//                .price(BigDecimal.valueOf(-1))
//                .build()
//        )
//    );
//  }
//
//  @DisplayName("상품 생성 -> 실패")
//  @ParameterizedTest
//  @MethodSource("wrongProducts1")
//  void SHOULD_fail_WHEN_create_Product(Product product) {
//    // 준비
//    given(productRepository.save(any())).willThrow(IllegalArgumentException.class);
//
//    // 실행
//    assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
//  }
//
  @DisplayName("상품 가격 수정 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_price_of_Product() {
    // 준비
    Product product = CoreMock.copy(PRODUCT_1);
    given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

    // 실행
    product.changePrice(BigDecimal.valueOf(10000));
    Product updatedProduct = productService.changePrice(product.getId(), product);

    // 검증
    assertThat(updatedProduct.getId()).isEqualTo(product.getId());
    assertThat(updatedProduct.getPrice()).isEqualTo(product.getPrice());
    assertThat(updatedProduct.getName()).isEqualTo(product.getName());
  }

  // TODO: Validate 로직 만들면 주석까지 지우기
//  static Stream<Arguments> wrongProducts2() {
//    return Stream.of(
//        arguments(new Product(UUID.randomUUID(), "데리버거", BigDecimal.valueOf(-1)), new IllegalArgumentException()),
//        arguments(new Product(UUID.randomUUID(), "데리버거", BigDecimal.valueOf(5000)), new NoSuchElementException())
//    );
//  }
//
//  @DisplayName("상품 가격 수정 -> 실패")
//  @ParameterizedTest
//  @MethodSource("wrongProducts2")
//  void SHOULD_fail_WHEN_change_price_of_Product(Product product, Exception e) {
//    // 준비
//    given(productRepository.findById(product.getId())).willThrow(e);
//
//    // 실행
//    assertThatThrownBy(() -> productService.changePrice(product.getId(), product)).isInstanceOf(e.getClass());
//  }
//
  @DisplayName("상품 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Products() {
    // 준비
    Product product = CoreMock.copy(PRODUCT_1);
    Product product2 = new Product.Builder()
        .id(UUID.randomUUID())
        .name("데리버거")
        .price(BigDecimal.valueOf(1000))
        .build();
    List<Product> productList = List.of(product, product2);

    given(productRepository.findAll()).willReturn(productList);

    // 실행
    List<Product> products = productRepository.findAll();

    // 검증
    assertThat(products).contains(product, product2);
  }
}
