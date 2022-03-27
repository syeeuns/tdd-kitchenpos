package kitchenpos.domain;

import java.util.Objects;
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
      OrderTable orderTable = new OrderTable(this);
      orderTable.validate();
      return orderTable;
    }
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void changeName(final String name) {
    validateName(name);
    this.name = name;
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public void changeNumberOfGuests(final int numberOfGuests) {
    validateNumberOfGuests(numberOfGuests);
    this.numberOfGuests = numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }
  
  public void changeEmpty(final boolean empty) {
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

  private void validate() {
    validateName(name);
  }

  private void validateName(String name) {
    if (isValidName(name)) {
      throw new IllegalArgumentException();
    }
  }

  private boolean isValidName(String name) {
    return Objects.isNull(name) || name.isEmpty();
  }

  private void validateNumberOfGuests(int numberOfGuests) {
    if (isValidNumberOfGuests(numberOfGuests)) {
      throw new IllegalArgumentException();
    }
  }

  private boolean isValidNumberOfGuests(int numberOfGuests) {
    return numberOfGuests < 0;
  }
}
