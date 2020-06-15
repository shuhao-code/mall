package com.imooc.mall.pojo;

import lombok.Data;

/**
 * @author shuhao
 * @date 2020/6/14 14:38
 */
@Data
public class Cart {

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }

    public Cart() {
    }
}
