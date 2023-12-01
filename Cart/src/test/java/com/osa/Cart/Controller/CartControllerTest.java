package com.osa.Cart.Controller;

import com.osa.Cart.Dto.CartItemDto;
import com.osa.Cart.Entity.Cart;
import com.osa.Cart.Entity.CartItem;
import com.osa.Cart.Service.CartService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    private CartItemDto cartItemDto;

    @Test
    public void testCreateCartSuccess() {
        Long cartId = 1L;

        when(cartService.createCart()).thenReturn(cartId);

        // Call the controller method
        ResponseEntity<Long> responseEntity = cartController.createCart();

        // Verify the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody());

        // Verify that the cartService.createCart method was called
        verify(cartService, times(1)).createCart();
    }

    @Test
    public void testCreateCartFailure() {
        // Simulate a failure scenario by throwing an exception
        when(cartService.createCart()).thenThrow(new RuntimeException("Cart creation failed"));

        // Call the controller method
        ResponseEntity<Long> responseEntity = cartController.createCart();

        // Verify the response status and body for failure scenario
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());  // Assuming the body is null for failure

        // Verify that the cartService.createCart method was called
        verify(cartService, times(1)).createCart();
    }

    @Test
    public void testGetCartById() {
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setCartId(cartId);

        when(cartService.getCartById(cartId)).thenReturn(Optional.of(cart));

        ResponseEntity<Cart> responseEntity = cartController.getCartById(cartId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cart, responseEntity.getBody());

        verify(cartService, times(1)).getCartById(cartId);
    }

    @Test
    public void testGetCartByIdNotFound() {
        Long cartId = 2L;

        when(cartService.getCartById(cartId)).thenReturn(Optional.empty());

        ResponseEntity<Cart> responseEntity = cartController.getCartById(cartId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(cartService, times(1)).getCartById(cartId);
    }

    @Test
    public void testAddCartItemSuccess() {
        // Mocking behavior for the cart service to return an Optional with a known Cart
        Long cartId = 1L;
        Cart cart = new Cart();

        when(cartService.getCartById(cartId)).thenReturn(Optional.of(cart));


        // Call the addCartItem method with valid cartItemDto and cartId
        ResponseEntity<String> response = cartController.addCartItem(cartItemDto, cartId);

        // Verify that the cartService.addCartItem method was called
        verify(cartService, times(1)).addCartItem(cartItemDto, cart);

        // Assert that the method returns an OK response with the expected message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cart item added successfully.", response.getBody());
    }


    @Test
    public void testAddCartItemFailureExceptionInService() {
        // Mocking behavior for the cart service to throw an exception
        Long cartId = 1L;
        Cart cart = new Cart();
        when(cartService.getCartById(cartId)).thenReturn(Optional.of(cart));
        doThrow(new RuntimeException("Some unexpected error")).when(cartService).addCartItem(any(), any());

        // Call the addCartItem method with valid cartItemDto and cartId
        ResponseEntity<String> response = cartController.addCartItem(cartItemDto, cartId);

        // Verify that the cartService.addCartItem method was called
        verify(cartService, times(1)).addCartItem(cartItemDto, cart);

        // Assert that the method returns a 500 internal server error response with the expected message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error adding cart item: Some unexpected error", response.getBody());
    }





}





