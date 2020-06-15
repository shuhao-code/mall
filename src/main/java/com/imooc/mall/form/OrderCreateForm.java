package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shuhao
 * @date 2020/6/15 22:44
 */
@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
