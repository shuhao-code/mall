package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import org.apache.ibatis.annotations.Param;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author shuhao
 * @date 2020/6/13 11:39
 */
//TODO 这个注解起到回滚的作用,不让测试的代码污染数据
//@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    public static final String USERNAME = "jack";
    public static final String PASSWORD = "123456";

    @Autowired
    private IUserService userService;

    @Test
    public void register() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail("jack@qq.com");
        user.setRole(RoleEnum.CUSTOMER.getCode());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        userService.register(user);
    }

    @Test
    public void login(){
        register();
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


}
