package com.milklabs.playground.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.milklabs.playground.dao.CategoryDAO;
import com.milklabs.playground.entity.Category;

@Service
public class CategoryService {

	private final CategoryDAO categoryDAO;

	private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

	public CategoryService(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public List<Category> findAll() {
		return StreamSupport.stream(categoryDAO.findAll().spliterator(), false).toList();
	}

	public List<Category> findAllCategory(String stringFilter) {
		log.debug("[findAllCategory] stringFilter: [{}]", stringFilter);

		if (stringFilter == null || stringFilter.isEmpty()) {
			return StreamSupport.stream(categoryDAO.findAll().spliterator(), false).toList();
		} else {
			return categoryDAO.findByCategoryName(stringFilter);
		}
	}

	public void deleteCategory(Category c) {
		categoryDAO.delete(c);
	}

	public void saveCategory(Category category) {
		if (category == null) {
			log.error("[saveCategory]  category null. Are you sure you have connected your form to the application?");
			return;
		}
		categoryDAO.save(category);
	}
	
	public List<Category> findAllPrecedingCategories(Integer categoryId) {
	    List<Category> precedingCategories = new ArrayList<>();
	    Category category = categoryDAO.findById(categoryId).orElse(null);
	    if (category != null) {
	        findAllPrecedingCategoriesRecursive(category, precedingCategories);
	    }
	    Collections.reverse(precedingCategories); 
	    return precedingCategories;
	}

	private void findAllPrecedingCategoriesRecursive(Category category, List<Category> precedingCategories) {
	    if (category != null) {
	        precedingCategories.add(category);
	        Category parentCategory = category.getCategoryParent();
	        if (parentCategory != null) {
	            findAllPrecedingCategoriesRecursive(parentCategory, precedingCategories);
	        }
	    }
	}

}
