package com.osa.Cart.Dto;

import com.osa.Cart.Entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CartItemDto   {
    private Long productId;
    private double price;
    private int quantity;
}
