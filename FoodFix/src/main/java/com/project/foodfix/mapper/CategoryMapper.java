package com.project.foodfix.mapper;

import com.project.foodfix.model.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;
public interface CategoryMapper {

    @Select("SELECT * FROM Category WHERE category_name = #{category_name}")
    Category getCategoryById(@Param("category_id") Integer category_id);

    @Select("SELECT * FROM Category WHERE category_id = #{category_id}")
    Category getCategoryByPhone(@Param("category_id") Integer category_id);

    @Select("SELECT * FROM Category")
    List<Category> getAllCategory();

    @Insert("INSERT INTO Category (category_id ,category_name) " +
            "VALUES (#{category_id}, #{category_name})")
    int insertCategory(Category category);

    @Update("UPDATE Category SET category_name=#{category_name}")
    int updateCategory(Category category);

    @Delete("DELETE FROM Category WHERE category_id=#{category_id}")
    int deleteCategory(@Param("category_id") Integer category_id);
}
