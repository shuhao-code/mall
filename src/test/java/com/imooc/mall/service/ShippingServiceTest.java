package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author shuhao
 * @date 2020/6/14 21:12
 */
@Slf4j
public class ShippingServiceTest extends MallApplicationTests {

    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    @Test
    public void add() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("书豪");
        form.setReceiverPhone("110");
        form.setReceiverProvince("河南");
        form.setReceiverCity("商丘市");
        form.setReceiverDistrict("柘城县");
        form.setReceiverAddress("慈圣镇");
        form.setReceiverZip("250000");
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
        log.info("{}", responseVo);
    }

    @Test
    public void delete(){
        Integer shippingId = 9;
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("{}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update(){
        Integer shippingId = 10;
        ShippingForm form = new ShippingForm();
        form.setReceiverName("韩书豪");
        ResponseVo responseVo = shippingService.update(uid, shippingId, form);
        log.info("{}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list(){
        ResponseVo<PageInfo> responseVo = shippingService.list(uid, 1, 10);
        log.info("{}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


}
