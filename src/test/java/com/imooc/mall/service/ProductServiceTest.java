package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shuhao
 * @date 2020/6/14 9:42
 */
@Slf4j
public class ProductServiceTest extends MallApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list(){
        ResponseVo<PageInfo> responseVoList = productService.list(null, 1, 2);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVoList.getStatus());
    }

    @Test
    public void detail(){
        ResponseVo<ProductDetailVo> productDetailVoResponseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), productDetailVoResponseVo.getStatus());
    }


}