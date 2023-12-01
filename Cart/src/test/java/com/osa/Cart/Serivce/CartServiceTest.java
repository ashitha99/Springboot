package com.osa.Cart.Serivce;

import com.osa.Cart.Entity.Cart;
import com.osa.Cart.Repository.CartRepository;
import com.osa.Cart.Service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @InjectMocks
    private CartService cartService;
    @Mock
    private CartRepository cartRepository;



    @Test
    public void testCreateCartFailure() {
        // Mocking behavior for the cart repository to throw an exception
        when(cartRepository.save(any(Cart.class))).thenThrow(new RuntimeException("Failed to save cart"));

        // Call the createCart method and assert that it throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.createCart());

        // Verify that the cart repository save method was called
        verify(cartRepository, times(1)).save(any(Cart.class));

        // Assert the expected exception message
        assertEquals("Failed to save cart Failed to save cart", exception.getMessage());
    }

    @Test
    public void testCreateCartWithGeneratedId() {
        // Mocking behavior for the cart repository to return a Cart with a generated cartId
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            cart.setCartId(1L); // Set a non-null cartId for testing purposes
            return cart;
        });

        // Call the createCart method
        Long cartId = cartService.createCart();

        // Verify that the cart repository save method was called
        verify(cartRepository, times(1)).save(any(Cart.class));

        // Assert that the returned cartId is not null and greater than 0
        assertNotNull(cartId, "Cart ID should not be null");
        assertTrue(cartId > 0, "Cart ID should be greater than 0");
    }
    @Test
    public void testGetExistingCartById() {
        // Mocking behavior for the cart repository to return an Optional with a known Cart
        Long cartId = 1L;
        Cart expectedCart = new Cart();
        expectedCart.setCartId(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(expectedCart));

        // Call the getCartById method
        Optional<Cart> result = cartService.getCartById(cartId);

        // Assert that the returned Optional is not empty and contains the expected Cart
        assertTrue(result.isPresent(), "Optional should not be empty");
        assertEquals(expectedCart, result.get(), "Returned Cart should match the expected Cart");
    }
    @Test
    public void testGetNonexistentCartById() {
        // Mocking behavior for the cart repository to return an empty Optional
        Long cartId = 1L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Call the getCartById method
        Optional<Cart> result = cartService.getCartById(cartId);

        // Assert that the returned Optional is empty
        assertTrue(result.isEmpty(), "Optional should be empty for nonexistent Cart");
    }
    @Test
    public void testGetCartByIdWithException() {
        // Mocking behavior for the cart repository to throw a specific exception
        Long cartId = 1L;
        when(cartRepository.findById(cartId)).thenThrow(new RuntimeException("Failed to retrieve cart"));

        // Call the getCartById method and assert that it throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.getCartById(cartId));

        // Assert the expected exception message
        assertEquals("Failed to retrieve cart", exception.getMessage());
    }





}
