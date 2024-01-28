package bookstore.repository.orderitem;

import bookstore.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    @Query("FROM OrderItem oi WHERE oi.id = :itemId "
            + "AND oi.order.id = :orderId AND oi.order.user.id = :userId")
    Optional<OrderItem> findByIdAndOrderIdForCurrentUser(Long userId, Long itemId, Long orderId);
}
