package com.imooc.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author shuhao
 * @date 2020/6/13 21:46
 */
@Data
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategory;
}
