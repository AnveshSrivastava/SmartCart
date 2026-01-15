package online.threadly.order_and_cart_management.service;

import lombok.AllArgsConstructor;
import online.threadly.order_and_cart_management.client.ProductClient;
import online.threadly.order_and_cart_management.dto.AddToCartRequest;
import online.threadly.order_and_cart_management.dto.AddToCartResponse;
import online.threadly.order_and_cart_management.dto.CartResponse;
import online.threadly.order_and_cart_management.dto.Product;
import online.threadly.order_and_cart_management.exception.BadRequestException;
import online.threadly.order_and_cart_management.model.Cart;
import online.threadly.order_and_cart_management.model.CartItem;
import online.threadly.order_and_cart_management.repository.CartItemRepository;
import online.threadly.order_and_cart_management.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public AddToCartResponse addToCart(UUID userId, AddToCartRequest request) {
        if(request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId()).orElseGet(() -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProductId(request.getProductId());
            newCartItem.setQuantity(0);
            return newCartItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);

        return new AddToCartResponse("Product added to cart successfully", cart.getId());
    }

    public CartResponse getCartForUser(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException("Cart not found for user."));

        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        List<UUID> productIds = cartItems.stream().map(CartItem::getProductId).toList();
        List<Product> products = productClient.getProductsByIds(productIds);

        CartResponse cartResponse = new CartResponse();
        double totalAmount = 0.0;
        for(CartItem cartItem : cartItems) {
            for(Product product : products) {
                if(cartItem.getProductId().equals(product.getId())) {
                    product.setQuantity(cartItem.getQuantity());
                    totalAmount += (product.getPrice() * cartItem.getQuantity());
                }
            }
        }
        cartResponse.setProducts(products);
        cartResponse.setTotalAmount(totalAmount);

        return cartResponse;
    }

}
