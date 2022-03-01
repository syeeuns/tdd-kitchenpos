package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
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

  public Order(UUID id, OrderType type, OrderStatus status, LocalDateTime orderDateTime,
      List<OrderLineItem> orderLineItems, String deliveryAddress,
      OrderTable orderTable) {
    this.id = id;
    this.type = type;
    this.status = status;
    this.orderDateTime = orderDateTime;
    this.orderLineItems = orderLineItems;
    this.deliveryAddress = deliveryAddress;
    this.orderTable = orderTable;
  }

  public Order(Order order) {
    this.id = order.getId();
    this.type = order.getType();
    this.status = order.getStatus();
    this.orderDateTime = order.getOrderDateTime();
    this.orderLineItems = order.getOrderLineItems();
    this.deliveryAddress = order.getDeliveryAddress();
    this.orderTable = order.getOrderTable();
  }

  public UUID getId() {
    return id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public OrderType getType() {
    return type;
  }

  public void setType(final OrderType type) {
    this.type = type;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(final OrderStatus status) {
    this.status = status;
  }

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public void setOrderDateTime(final LocalDateTime orderDateTime) {
    this.orderDateTime = orderDateTime;
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }

  public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
    this.orderLineItems = orderLineItems;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(final String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public OrderTable getOrderTable() {
    return orderTable;
  }

  public void setOrderTable(final OrderTable orderTable) {
    this.orderTable = orderTable;
  }

  public UUID getOrderTableId() {
    return orderTableId;
  }

  public void setOrderTableId(final UUID orderTableId) {
    this.orderTableId = orderTableId;
  }
}
