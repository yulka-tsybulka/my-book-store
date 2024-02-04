package mate.academy.bookstore.repository.shoppingcart;

import java.util.Optional;
import mate.academy.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface ShoppingCartRepository
        extends JpaRepository<ShoppingCart, Long>, JpaSpecificationExecutor<ShoppingCart> {
    Optional<ShoppingCart> findByUserId(@PathVariable(name = "id") Long userId);
}
