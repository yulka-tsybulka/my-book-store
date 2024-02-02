package mate.academy.bookstore.repository.order;

import java.util.Optional;
import mate.academy.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findAllByUserId(Long userId);

    @Query("FROM Order o WHERE o.id = :orderId AND o.user.id = :userId")
    Optional<Order> findByIdForCurrentUser(Long userId, Long orderId);
}
