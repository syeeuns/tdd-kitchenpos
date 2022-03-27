package kitchenpos.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable request) {
        final String name = request.getName();
        final OrderTable orderTable = new OrderTable.Builder()
            .id(UUID.randomUUID())
            .name(name)
            .numberOfGuests(0)
            .empty(true)
            .build();
        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable sit(final UUID orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoSuchElementException::new);
        orderTable.changeEmpty(false);
        return orderTable;
    }

    @Transactional
    public OrderTable clear(final UUID orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoSuchElementException::new);
        if (orderRepository.existsByOrderTableAndStatusNot(orderTable, OrderStatus.COMPLETED)) {
            throw new IllegalStateException();
        }
        orderTable.changeNumberOfGuests(0);
        orderTable.changeEmpty(true);
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final UUID orderTableId, final OrderTable request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoSuchElementException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalStateException();
        }
        orderTable.changeNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAll() {
        return orderTableRepository.findAll();
    }
}
