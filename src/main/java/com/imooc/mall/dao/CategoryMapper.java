package com.imooc.mall.dao;

import com.imooc.mall.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author shuhao
 * @date 2020/6/11 11:12
 */
//@Mapper
public interface CategoryMapper {
    
    @Select("select * from mall_category where id = #{id}")
    public Category findById(@Param("id") Integer id);
    
    public Category queryById(@Param("id") Integer id);
}
