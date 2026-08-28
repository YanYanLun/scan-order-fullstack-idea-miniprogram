package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.entity.Category;
import com.scanorder.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/categories", "/api/categories"})
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public Result<List<Category>> getCategories() {
        return Result.success(categoryRepository.findAllByOrderBySortOrderAsc());
    }

    @PostMapping
    public Result<Category> createCategory(@RequestBody Category category) {
        if (category.getId() == null || category.getId().isEmpty()) {
            category.setId("cat_" + UUID.randomUUID().toString().substring(0, 8));
        }
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        categoryRepository.save(category);
        return Result.success("分类创建成功", category);
    }

    @PutMapping("/{id}")
    public Result<Category> updateCategory(@PathVariable(name = "id") String id, @RequestBody Category category) {
        return categoryRepository.findById(id).map(existing -> {
            if (category.getName() != null) existing.setName(category.getName());
            if (category.getSortOrder() != null) existing.setSortOrder(category.getSortOrder());
            if (category.getIsEnabled() != null) existing.setIsEnabled(category.getIsEnabled());
            categoryRepository.save(existing);
            return Result.success("分类更新成功", existing);
        }).orElse(Result.error("分类不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable(name = "id") String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return Result.success("删除分类成功", id);
        }
        return Result.error("分类不存在");
    }
}
