package com.imooc.mall.controller;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.form.UserRegisterForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROE;
import static com.imooc.mall.enums.ResponseEnum.USERNAME_OR_PASSWORD_ERROR;

/**
 * @author shuhao
 * @date 2020/6/13 12:03
 */
@RestController
@RequestMapping
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    //这种接收方方式是通过form-urlencode方式进行请求参数的接收,不推荐
    //(@RequestParam(value="username") String userName)这是一种接收方式
    //(User user)直接这种方式也可以进行封装

    //第二种接收方式是JSON:applicaiton/json
    //(@RequestBody User user)必须这种形式才可以
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("注册提交的参数有误, {} {}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROE,bindingResult);
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);


        return userService.register(user);
    }


    //用户登录实现
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  BindingResult bindingResult,
                                  HttpSession session){

        if(bindingResult.hasErrors()){
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        //设置session
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());
        log.info("/user/login sessionId = {}", session.getId());

        return userResponseVo;
    }

    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId = {}", session.getId());
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);

        return ResponseVo.success(user);
    }

    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        log.info("/user/logout sessionId = {}", session.getId());

        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }

}
