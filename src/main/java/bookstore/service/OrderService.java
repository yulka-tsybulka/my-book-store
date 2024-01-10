package bookstore.service;

import bookstore.dto.order.OrderDto;
import bookstore.dto.order.OrderItemDto;
import bookstore.dto.order.PlaceOrderRequestDto;
import bookstore.dto.order.UpdateStatusRequestDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(Long userId, PlaceOrderRequestDto requestDto);

    List<OrderDto> getAll(Long userId, Pageable pageable);

    OrderDto updateOrderStatus(Long orderId, UpdateStatusRequestDto statusRequestDto);

    Set<OrderItemDto> getAllOrderItems(Long userId, Long orderId, Pageable pageable);

    OrderItemDto getItemById(Long userId, Long orderId, Long itemId);
}

