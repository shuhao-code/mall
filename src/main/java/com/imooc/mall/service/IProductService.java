package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author shuhao
 * @date 2020/6/14 9:01
 */
public interface IProductService {

    ResponseVo<PageInfo> list(Integer id, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer categoryId);
}
