package com.osa.Cart.Service;

import com.osa.Cart.Entity.Cart;
import com.osa.Cart.Entity.CartItem;
import com.osa.Cart.Repository.CartItemRepository;
import com.osa.Cart.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;

    public double calculateTotalPrice(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        double totalCartPrice = 0.0;

        for (CartItem cartItem : cartItems) {
            totalCartPrice += cartItem.getSubtotal();
        }

        return totalCartPrice;
    }

    public void updateTotalPrice(Optional<Cart> optionalCart) {
        optionalCart.ifPresent(cart -> {
            try {
                // Calculate the total price based on the current cart items
                double totalCartPrice = calculateTotalPrice(cart);

                // Update the cart's total price
                cart.setTotalPrice(totalCartPrice);

                // Save the updated cart
                cartRepository.save(cart);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error while updating total price.", e);
            }
        });
    }


}

