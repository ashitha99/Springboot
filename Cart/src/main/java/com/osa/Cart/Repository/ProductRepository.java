package com.osa.Cart.Repository;

import com.osa.Cart.Entity.CartItem;
import com.osa.Cart.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
