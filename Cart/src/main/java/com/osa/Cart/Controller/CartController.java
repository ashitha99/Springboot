package com.osa.Cart.Controller;

import com.osa.Cart.Dto.CartItemDto;
import com.osa.Cart.Entity.Cart;
import com.osa.Cart.Entity.CartItem;
import com.osa.Cart.Service.CartItemService;
import com.osa.Cart.Service.CartService;
import com.osa.Cart.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.ScatteringByteChannel;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;  // Assuming you have a CartService

    @Autowired
    private CartItemService cartItemService;

   @PostMapping("/createCart")
   public ResponseEntity<Long> createCart() {
       try {
           Long cartId = cartService.createCart();
           return ResponseEntity.ok(cartId);
       } catch (RuntimeException e) {
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }
   }

    @GetMapping("/viewCart/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


   @PostMapping("/addCartItem/{cartId}")
    public ResponseEntity<String> addCartItem(
            @RequestBody CartItemDto cartItemDto,
            @PathVariable Long cartId) {
        try {
            // Fetch the cart by ID
            Optional<Cart> cart = cartService.getCartById(cartId);

            // Check if cart is present, then add the cart item to the cart and update total price
            cart.ifPresent(value -> cartService.addCartItem(cartItemDto, value));

            return ResponseEntity.ok("Cart item added successfully.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding cart item: " + e.getMessage());
        }
    }

   /* @DeleteMapping("/removeCartItem/{cartItemId}/{quantityToRemove}")
    public ResponseEntity<String> removeCartItemAndUpdateTotalPrice(
            @PathVariable Long cartItemId,
            @PathVariable int quantityToRemove) {
        try {
            cartService.removeCartItemAndUpdateTotalPrice(cartItemId, quantityToRemove);
            return ResponseEntity.ok("Cart item removed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing cart item: " + e.getMessage());
        }
    }*/


}






