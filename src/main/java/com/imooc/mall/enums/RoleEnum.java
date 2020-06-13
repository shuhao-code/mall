package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author shuhao
 * @date 2020/6/13 11:47
 */
@Getter
public enum RoleEnum {
    ADMIN(0),

    CUSTOMER(1),

    ;

    Integer code;

    RoleEnum(Integer code){
        this.code = code;
    }
}
