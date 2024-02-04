package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.shoppingcart.AddToCartRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.dto.shoppingcart.UpdateQuantityBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCartDto addToCart(Long userId, AddToCartRequestDto requestDto) {
        Long bookId = requestDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is not found book with id " + bookId));
        ShoppingCart shoppingCartFromDb = getShoppingCartByUserId(userId);
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCartFromDb);
        cartItemRepository.save(cartItem);
        shoppingCartFromDb.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartFromDb);
    }

    @Override
    public ShoppingCartDto findById(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateQuantityByCartItemId(
            Long userId, Long cartItemId, UpdateQuantityBookRequestDto requestDto) {
        ShoppingCart shoppingCartFromDb = getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        shoppingCartFromDb.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartFromDb);
    }

    @Override
    public ShoppingCartDto deleteCartItemById(Long userId, Long cartItemId) {
        ShoppingCart shoppingCartFromDb = getShoppingCartByUserId(userId);
        shoppingCartFromDb.getCartItems()
                .remove(cartItemRepository.findCartItemById(cartItemId));
        return shoppingCartMapper.toDto(shoppingCartFromDb);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is not found user with id " + userId));
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setUser(user);
                    shoppingCartRepository.save(shoppingCart);
                    return shoppingCart;
                });
    }
}
