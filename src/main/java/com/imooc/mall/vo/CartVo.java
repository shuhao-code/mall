package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shuhao
 * @date 2020/6/14 11:49
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private Boolean selectAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuatity;
}
