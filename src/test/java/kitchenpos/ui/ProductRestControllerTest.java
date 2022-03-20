package kitchenpos.ui;

import static kitchenpos.mocker.CoreMock.NEGATIVE_PRICE;
import static kitchenpos.mocker.CoreMock.PRODUCT_1;
import static kitchenpos.mocker.CoreMock.PRODUCT_2;
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
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
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

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private ProductService productService;


  @DisplayName("상품 생성 -> 성공")
  @Test
  void SHOULD_success_WHEN_create_Product() throws Exception {
    // 준비
    given(productService.create(any())).willReturn(PRODUCT_1);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(PRODUCT_1))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("name").value(PRODUCT_1.getName()));
  }

  static Stream<Arguments> wrongProducts() {
    return Stream.of(
        arguments(new Product(UUID.randomUUID(), "", BigDecimal.valueOf(5000))),
        arguments(new Product(UUID.randomUUID(), "데리버거", NEGATIVE_PRICE))
    );
  }

  @DisplayName("상품 생성 -> 실패")
  @ParameterizedTest
  @MethodSource("wrongProducts")
  void SHOULD_fail_WHEN_create_Product(Product product) throws Exception {
    // 준비
    given(productService.create(any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(
        post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product))
            .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("상품 가격 수정 -> 성공")
  @Test
  void SHOULD_success_WHEN_change_price_of_Product() throws Exception{
    // 준비
    final BigDecimal CHANGED_PRICE = BigDecimal.valueOf(10000);
    Product priceChangedProduct = Product.of(PRODUCT_1);
    priceChangedProduct.setPrice(CHANGED_PRICE);

    given(productService.changePrice(any(), any())).willReturn(priceChangedProduct);

    // 실행
    ResultActions perform = mockMvc.perform(put(String.format("/api/products/%s/price", PRODUCT_1.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(PRODUCT_1))
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("price").value(CHANGED_PRICE));
  }

  @DisplayName("상품 가격 수정 -> 실패")
  @Test
  void SHOULD_fail_WHEN_change_price_of_Product() throws Exception{
    // 준비
    final BigDecimal CHANGED_PRICE = NEGATIVE_PRICE;
    Product priceChangedProduct = Product.of(PRODUCT_1);
    priceChangedProduct.setPrice(CHANGED_PRICE);

    given(productService.changePrice(any(), any())).willThrow(IllegalArgumentException.class);

    // 실행
    ResultActions perform = mockMvc.perform(put(String.format("/api/products/%s/price", priceChangedProduct.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(priceChangedProduct))
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().is4xxClientError());
  }

  @DisplayName("상품 전체 조회 -> 성공")
  @Test
  void SHOULD_success_WHEN_findAll_Products() throws Exception{
    // 준비
    List<Product> productList = List.of(PRODUCT_1, PRODUCT_2);

    given(productService.findAll()).willReturn(productList);

    // 실행
    ResultActions perform = mockMvc.perform(get("/api/products")
        .accept(MediaType.APPLICATION_JSON));

    // 검증
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(PRODUCT_1.getId().toString()))
        .andExpect(jsonPath("$.[0].name").value(PRODUCT_1.getName()))
        .andExpect(jsonPath("$.[0].price").value(PRODUCT_1.getPrice()))
        .andExpect(jsonPath("$.[1].id").value(PRODUCT_2.getId().toString()))
        .andExpect(jsonPath("$.[1].name").value(PRODUCT_2.getName()))
        .andExpect(jsonPath("$.[1].price").value(PRODUCT_2.getPrice()));
  }
}
