package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.form.ShippingForm;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author shuhao
 * @date 2020/6/14 20:39
 */
public interface IShippingService {

    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
