package com.imooc.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shuhao
 * @date 2020/6/14 14:45
 */
@Slf4j
public class CartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, form);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list(){
        ResponseVo<CartVo> list = cartService.list(1);
        log.info("list={}", gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());
    }


    @Test
    public void update(){
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);
        form.setSelected(false);
        ResponseVo<CartVo> update = cartService.update(1, 26, form);
        log.info("log={}", gson.toJson(update));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), update.getStatus());
    }


    @Test
    public void delete(){
        ResponseVo<CartVo> delete = cartService.delete(1, 26);
        log.info("log={}", gson.toJson(delete));
    }


    @Test
    public void selectAll(){
        ResponseVo<CartVo> responseVo = cartService.selectAll(1);
        log.info("{}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


    @Test
    public void unSelectAll(){
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(1);
        log.info("{}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


    @Test
    public void sum(){
        ResponseVo<Integer> sum = cartService.sum(1);
        log.info("{}", gson.toJson(sum));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), sum.getStatus());
    }


}
