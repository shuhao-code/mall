package com.imooc.mall.exception;

import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.imooc.mall.enums.ResponseEnum.ERROR;
import static com.imooc.mall.enums.ResponseEnum.NEED_LOGIN;

/**
 * @author shuhao
 * @date 2020/6/13 14:40
 */
@ControllerAdvice
public class RuntimeExceptionHandle {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handle(RuntimeException e){
        String message = e.getMessage();
        return ResponseVo.error(ERROR, message);
    }

    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle(RuntimeException e){
        return ResponseVo.error(NEED_LOGIN);
    }
}
