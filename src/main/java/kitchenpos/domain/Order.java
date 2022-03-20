package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "orders")
@Entity
public class Order {

  @Column(name = "id", columnDefinition = "varbinary(16)")
  @Id
  private UUID id;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderType type;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "order_date_time", nullable = false)
  private LocalDateTime orderDateTime;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(
      name = "order_id",
      nullable = false,
      columnDefinition = "varbinary(16)",
      foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
  )
  private List<OrderLineItem> orderLineItems;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  @ManyToOne
  @JoinColumn(
      name = "order_table_id",
      columnDefinition = "varbinary(16)",
      foreignKey = @ForeignKey(name = "fk_orders_to_order_table")
  )
  private OrderTable orderTable;

  @Transient
  private UUID orderTableId;

  public Order() {
  }

  public Order(Builder builder) {
    this.id = builder.id;
    this.type = builder.type;
    this.status = builder.status;
    this.orderDateTime = builder.orderDateTime;
    this.orderLineItems = builder.orderLineItems;
    this.deliveryAddress = builder.deliveryAddress;
    this.orderTable = builder.orderTable;
  }

  public UUID getId() {
    return id;
  }

  public OrderType getType() {
    return type;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void changeStatus(final OrderStatus status) {
    setStatus(status);
  }

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public OrderTable getOrderTable() {
    return orderTable;
  }

  public UUID getOrderTableId() {
    return orderTableId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(id, order.id) && type == order.type
        && status == order.status
        && Objects.equals(orderDateTime, order.orderDateTime)
        && orderLineItems.equals(order.orderLineItems)
        && deliveryAddress.equals(order.deliveryAddress)
        && orderTable.equals(order.orderTable);
  }
  @Override
  public int hashCode() {
    return Objects
        .hash(id, type, status, orderDateTime, orderLineItems, deliveryAddress, orderTable);
  }

  public static class Builder {
    private UUID id;
    private OrderType type;
    private OrderStatus status;
    private LocalDateTime orderDateTime;
    private List<OrderLineItem> orderLineItems;
    private String deliveryAddress;
    private OrderTable orderTable;
    private UUID orderTableId;

    public Builder() {
    }

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder type(OrderType type) {
      this.type = type;
      return this;
    }

    public Builder status(OrderStatus status) {
      this.status = status;
      return this;
    }

    public Builder orderDateTime(LocalDateTime orderDateTime) {
      this.orderDateTime = orderDateTime;
      return this;
    }

    public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
      this.orderLineItems = orderLineItems;
      return this;
    }

    public Builder deliveryAddress(String deliveryAddress) {
      this.deliveryAddress = deliveryAddress;
      return this;
    }

    public Builder orderTable(OrderTable orderTable) {
      this.orderTable = orderTable;
      return this;
    }

    public Builder orderTableId(UUID orderTableId) {
      this.orderTableId = orderTableId;
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }

  private void setStatus(final OrderStatus status) {
    this.status = status;
  }
}
