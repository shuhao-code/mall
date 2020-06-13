package com.imooc.mall.service.impl;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author shuhao
 * @date 2020/6/13 22:01
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {

        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();

        //查出parent_id = 0
        for(Category category : categories){
            if(category.getParentId().equals(MallConst.ROOT_PARENT_ID)){
                CategoryVo categoryVo = category2CategoryVo(category);
                categoryVoList.add(categoryVo);
            }
        }
        categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        findSubCategory(categoryVoList, categories);

        return ResponseVo.success(categoryVoList);
    }

    //查找子目录
    public void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categories){
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryList = new ArrayList<>();
            for (Category category : categories) {
                if(categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryList.add(subCategoryVo);
                }
            }
            subCategoryList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
            categoryVo.setSubCategory(subCategoryList);

            findSubCategory(subCategoryList, categories);
        }
    }


    /**
     * 将Category对象转换为CategoryVo对象
     * @param category
     * @return
     */
    public CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }


}
