package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.service.IProductService;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.imooc.mall.enums.ProductStatusEnum.DELECT;
import static com.imooc.mall.enums.ProductStatusEnum.OFF_SALE;

/**
 * @author shuhao
 * @date 2020/6/14 9:04
 */
@Service
@Slf4j
public class ProdectServiceImpl implements IProductService {

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if(categoryId != null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }
        //分页设置
        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
//        log.info("prosucts={}", products);
        //以下过程实现从product转换到productVo的过程
        List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product, productVo);
            productVoList.add(productVo);
        }

        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        //把结果封装好返回
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer categoryId) {

        Product product = productMapper.selectByPrimaryKey(categoryId);

        //针对确定条件进行判断
        if(product.getStatus().equals(OFF_SALE.getCode()) || product.getStatus().equals(DELECT.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELECT);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        //敏感数据处理
        productDetailVo.setStock(productDetailVo.getStock() > 100 ? 100 : productDetailVo.getStock());
        return ResponseVo.success(productDetailVo);
    }


}
