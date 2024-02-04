package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.shoppingcart.AddToCartRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.dto.shoppingcart.UpdateQuantityBookRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto addToCart(Long userId, AddToCartRequestDto bookRequestDto);

    ShoppingCartDto findById(Long userId);

    ShoppingCartDto updateQuantityByCartItemId(
            Long userId, Long cartItemId, UpdateQuantityBookRequestDto requestDto);

    ShoppingCartDto deleteCartItemById(Long userId, Long cartItemId);
}
