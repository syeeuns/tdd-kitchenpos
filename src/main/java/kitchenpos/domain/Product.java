package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "product")
@Entity
public class Product {
    @Column(name = "id", columnDefinition = "varbinary(16)")
    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public Product() {
    }

    public Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private BigDecimal price;

        public Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product(this);
            product.validate();
            return product;
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id)
            && Objects.equals(name, product.name)
            && Objects.equals(price, product.price);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    private void validate() {
        validateName(name);
        validatePrice(price);
    }

    private void validateName(String name) {
        if (isValidName(name)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidName(String name) {
        return Objects.isNull(name) || name.isEmpty();
    }

    private void validatePrice(BigDecimal price) {
        if (isValidPrice(price)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidPrice(BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }
}
