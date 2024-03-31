package com.example.forum.mapper;


import com.example.forum.dto.Category.CategoryDTO;
import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses={TopicMapper.class})
public interface CategoryMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "subcategories", source = "subcategories")
    @Mapping(target = "topics", source = "topics")
    @Mapping(source = "authorEmail", target = "authorEmail")
    CategoryDTO toDTO(Category category);

    List<CategoryDTO> toDTOList(List<Category> categories);

}
