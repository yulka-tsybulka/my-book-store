package mate.academy.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.OrderItemDto;
import mate.academy.bookstore.dto.order.PlaceOrderRequestDto;
import mate.academy.bookstore.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.orderitem.OrderItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(Long userId, PlaceOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                "There is not found shopping cart for user with id " + userId));
        Order order = createOrder(userId, requestDto, shoppingCart);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId,
                                      UpdateStatusRequestDto statusRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is not found order with id " + orderId));
        order.setStatus(statusRequestDto.getStatus());
        return orderMapper.toDto(order);
    }

    @Override
    public Set<OrderItemDto> getAllOrderItems(Long userId, Long orderId, Pageable pageable) {
        Order order = orderRepository
                .findByIdForCurrentUser(userId, orderId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Order with id " + orderId
                                + " not found for user with id " + userId));
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItemMapper.toDtoSet(orderItems);
    }

    @Override
    public OrderItemDto getItemById(Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdForCurrentUser(userId, itemId, orderId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "OrderItem with id " + itemId
                                + " not found in order with id " + orderId
                                + " for user with id " + userId));
        return orderItemMapper.toDto(orderItem);
    }

    private Order createOrder(Long userId,
                              PlaceOrderRequestDto requestDto,
                              ShoppingCart shoppingCart) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is not found user with id " + userId));
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        Set<OrderItem> orderItems = new HashSet<>();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new EntityNotFoundException(
                    "There is not found any cart items in the shopping cart of user with id "
                            + userId);
        }
        BigDecimal total = getTotal(cartItems, order, orderItems);
        order.setTotal(total);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setOrderItems(orderItems);
        return order;
    }

    private static BigDecimal getTotal(Set<CartItem> cartItems,
                                       Order order,
                                       Set<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem: cartItems) {
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();
            BigDecimal price = cartItem.getBook().getPrice();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setPrice(price);
            orderItem.setQuantity(quantity);
            orderItems.add(orderItem);
            total.add(price.multiply(BigDecimal.valueOf(quantity)));
            cartItem.setDeleted(true);
        }
        return total;
    }
}
