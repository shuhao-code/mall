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

    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误");

    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

}