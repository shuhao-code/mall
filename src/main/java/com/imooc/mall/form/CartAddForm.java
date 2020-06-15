package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shuhao
 * @date 2020/6/14 11:59
 *
 * 添加商品
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;
}
