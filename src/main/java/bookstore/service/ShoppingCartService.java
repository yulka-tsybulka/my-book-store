package bookstore.service;

import bookstore.dto.shoppingcart.AddToCartRequestDto;
import bookstore.dto.shoppingcart.ShoppingCartDto;
import bookstore.dto.shoppingcart.UpdateQuantityBookRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto addToCart(Long userId, AddToCartRequestDto bookRequestDto);

    ShoppingCartDto findById(Long userId);

    ShoppingCartDto updateQuantityByCartItemId(
            Long userId, Long cartItemId, UpdateQuantityBookRequestDto requestDto);

    ShoppingCartDto deleteCartItemById(Long userId, Long cartItemId);
}
