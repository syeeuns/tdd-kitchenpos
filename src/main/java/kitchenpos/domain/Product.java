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

    public void isValid(String name, BigDecimal price) {
        isValidName(name);
        isValidPrice(price);
    }

    private void isValidName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void isValidPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Product of(Product product) {
        return new Product(product.getId(), product.getName(), product.getPrice());
    }

    // getter, setter, constructor
    public Product() {
    }

    public Product(UUID id, String name, BigDecimal price) {
//        isValid(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(Builder builder) {
//        isValid(builder.name, builder.price);
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
            return new Product(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
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
}
