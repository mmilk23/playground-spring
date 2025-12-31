package com.milklabs.playground.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.milklabs.playground.entity.Category;

public interface CategoryDAO extends CrudRepository<Category, Integer> {
	
	@Query("select c from Category c where lower(c.categoryName) like lower(concat('%', :searchTerm, '%'))") 
	List<Category> findByCategoryName(String searchTerm);
	
	Category findByCategoryId(Integer id);

	@Query("SELECT c FROM Category c WHERE c.categoryId = :categoryId OR c.categoryParent.categoryId = :categoryId")
    List<Category> findAllPrecedingCategories(@Param("categoryId") Integer categoryId);
	
}
