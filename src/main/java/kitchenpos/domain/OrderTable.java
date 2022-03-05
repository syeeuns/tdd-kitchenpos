package kitchenpos.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_table")
@Entity
public class OrderTable {

  @Column(name = "id", columnDefinition = "varbinary(16)")
  @Id
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "number_of_guests", nullable = false)
  private int numberOfGuests;

  @Column(name = "empty", nullable = false)
  private boolean empty;

  public OrderTable() {
  }

  public OrderTable(OrderTable orderTable) {
    this.id = orderTable.id;
    this.name = orderTable.name;
    this.numberOfGuests = orderTable.numberOfGuests;
    this.empty = orderTable.empty;
  }

  public OrderTable(UUID id, String name, int numberOfGuests, boolean empty) {
    this.id = id;
    this.name = name;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTable(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.numberOfGuests = builder.numberOfGuests;
    this.empty = builder.empty;
  }

  public static class Builder {

    private UUID id;
    private String name;
    private int numberOfGuests;
    private boolean empty;

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

    public Builder numberOfGuests(int numberOfGuests) {
      this.numberOfGuests = numberOfGuests;
      return this;
    }

    public Builder empty(boolean empty) {
      this.empty = empty;
      return this;
    }

    public OrderTable build() {
      return new OrderTable(this);
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

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public void setNumberOfGuests(final int numberOfGuests) {
    this.numberOfGuests = numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }

  public void setEmpty(final boolean empty) {
    this.empty = empty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    if (numberOfGuests == 0) {
      OrderTable that = (OrderTable) o;
      return numberOfGuests == that.numberOfGuests
          && empty == that.empty
          && id.equals(that.id)
          && name.equals(that.name);
    }

    OrderTable that = (OrderTable) o;
    return empty == that.empty
        && id.equals(that.id)
        && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, numberOfGuests, empty);
  }
}
