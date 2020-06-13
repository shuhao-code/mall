package com.imooc.mall.service;

import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author shuhao
 * @date 2020/6/13 21:59
 */
public interface ICategoryService {

    ResponseVo<List<CategoryVo>> selectAll();
}
