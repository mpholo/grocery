package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.Category;
import com.mpholo.project.grocery.mapper.CategoryMapper;
import com.mpholo.project.grocery.model.CategoryDTO;
import com.mpholo.project.grocery.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/18
 IDE IntelliJ IDEA
 *******************************************************************/

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Optional<CategoryDTO> findByCategoryName(String name) {
        return categoryRepository.findByCategoryName(name).
                map(categoryMapper::CategoryToCategoryDTO);
    }

    @Override
    public Optional<CategoryDTO> findById(Integer id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::CategoryToCategoryDTO);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::CategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
           categoryRepository.deleteById(id);
    }

    @Override
    public void delete(CategoryDTO categoryDTO) {
        categoryRepository.delete(categoryMapper.CategoryDTOToCategory(categoryDTO));
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {

        return saveAndReturnDTO(categoryDTO);
    }

    @Override
    public CategoryDTO edit(Integer id, CategoryDTO categoryDTO) {
        return saveAndReturnDTO(categoryDTO);
    }

    private CategoryDTO saveAndReturnDTO(CategoryDTO categoryDTO) {
        Category category = categoryMapper.CategoryDTOToCategory(categoryDTO);
        Category savedCategory= categoryRepository.save(category);
        return  categoryMapper.CategoryToCategoryDTO(savedCategory);
    }
}
