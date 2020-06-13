package com.imooc.mall.service.impl;

import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author shuhao
 * @date 2020/6/13 11:18
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     * @param user
     */
    @Override
    public ResponseVo<User> register(User user) {

//        error();

        //添加用户角色
        user.setRole(RoleEnum.CUSTOMER.getCode());
        //用户名验证,用户名不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername != 0){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }

        //邮箱验证,不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail != 0){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }

        //MD5摘要算法(Spring自带,无需网上下载)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //加入数据库user表中
        int resultCount = userMapper.insertSelective(user);
        if(resultCount == 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }


        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        //判断用户是否存在
        if(user == null){
            //用户不存在,提示信息为:用户名或密码错误
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        //判断用户密码是否正确
        if(!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))){
            //密码不正确,提示信息为:用户名或密码错误
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        //这里返回之前要把用户的密码设置为空
        user.setPassword("");
        return ResponseVo.success(user);
    }

    private void error() {
        throw new RuntimeException("程序出现异常");
    }
}
