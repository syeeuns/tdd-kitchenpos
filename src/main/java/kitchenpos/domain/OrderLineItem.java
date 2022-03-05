package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "menu_id",
        columnDefinition = "varbinary(16)",
        foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu")
    )
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Transient
    private UUID menuId;

    @Transient
    private BigDecimal price;

    public OrderLineItem() {
    }
    public OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.menu = builder.menu;
        this.quantity = builder.quantity;
        this.menuId = builder.menuId;
        this.price = builder.price;
    }

    public static class Builder {

        private Long seq;
        private Menu menu;
        private long quantity;
        private UUID menuId;
        private BigDecimal price;

        public Builder() {
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder menuId(UUID menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public UUID getMenuId() {
        return menuId;
    }

    public void setMenuId(final UUID menuId) {
        this.menuId = menuId;
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
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity
            && Objects.equals(seq, that.seq)
            && menu.equals(that.menu);
    }
    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, quantity);
    }
}
