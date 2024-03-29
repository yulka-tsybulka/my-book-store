package mate.academy.bookstore.repository.cartitem;

import mate.academy.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {

    CartItem findCartItemById(Long id);
}
