package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author shuhao
 * @date 2020/6/14 10:58
 *
 * 商品状态.1-在售 2-下架 3-删除
 */
@Getter
public enum ProductStatusEnum {

    ON_SALE(1),

    OFF_SALE(2),

    DELECT(3)
    ;

    Integer code;
    ProductStatusEnum(Integer code){
        this.code = code;
    }
}
