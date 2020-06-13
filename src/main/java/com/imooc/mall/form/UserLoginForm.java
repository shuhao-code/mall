package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shuhao
 * @date 2020/6/13 13:34
 */
@Data
public class UserLoginForm {

    @NotBlank  //用于判断字符串 String
    private String username;

    @NotBlank
    private String password;

}
