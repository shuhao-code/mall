package com.imooc.mall.enums;

import com.imooc.mall.vo.ResponseVo;
import lombok.Getter;

/**
 * @author shuhao
 * @date 2020/6/13 13:12
 */
@Getter
public enum ResponseEnum {

    ERROR(-1, "服务端异常"),

    SUCCESS(0,"成功"),

    PASSWORD_ERROR(1, "密码错误"),

    USERNAME_EXIST(2, "用户已存在"),

    PARAM_ERROE(3, "参数错误"),

    EMAIL_EXIST(4, "邮箱已存在"),

    NEED_LOGIN(10, "用户未登录,请先登录"),

    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误"),

    PRODUCT_OFF_SALE_OR_DELECT(12, "商品下架或删除"),

    PRODUCT_NOT_EXIST(13, "商品不存在"),

    PRODUCT_STOCK_ERROR(14, "库存不正确"),

    CART_PRODUCT_NOT_EXIST(15, "购物车里没有该商品"),

    DELETE_SHIPPING_FAIL(16, "删除地址失败"),

    SHIPPING_NOT_EXIST(17, "收货地址不存在"),

    CART_SELECTED_IS_EMPTY(18, "请选择商品后下单"),

    PRODUCT_OFF_SALE_OR_DELETE(19, "商品已下架或删除"),

    PROODUCT_STOCK_ERROR(20, "商品库存不足"),

    ORDER_NOT_EXIST(21, "订单不存在"),

    ORDER_STATUS_ERROR(22, "订单状态不正确"),
    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

}
