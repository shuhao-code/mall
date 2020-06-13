package com.imooc.mall;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.exception.UserLoginException;
import com.imooc.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author shuhao
 * @date 2020/6/13 20:35
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {


    /**
     * 设置登录拦截器
     *
     * 返回值 false 表示停止
     * 返回值 true  表示不拦截,继续以下代码
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle......");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        if(user == null){
            log.info("user=null");

            throw new UserLoginException();
//            return false;
        }

        return true;
    }
}