package com.imooc.mall.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author shuhao
 * @date 2020/6/11 11:09
 */
@Data
public class Category {
    
    private Integer id;
    
    private Integer parentId;
    
    private String name;
    
    private Integer status;
    
    private Integer sortOrder;
    
    private Date createTime;
    
    private Date updateTime;
}
