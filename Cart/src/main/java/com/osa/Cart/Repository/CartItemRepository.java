package com.osa.Cart.Repository;

import com.osa.Cart.Entity.Cart;
import com.osa.Cart.Entity.CartItem;
import com.osa.Cart.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByProduct(Product product);

    List<CartItem> findByCart(Cart cart);

}
