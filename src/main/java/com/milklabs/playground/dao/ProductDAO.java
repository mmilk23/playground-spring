package com.milklabs.playground.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.milklabs.playground.entity.Product;

public interface ProductDAO extends CrudRepository<Product, Integer> {

	@Query("select p from Product p where lower(p.productName) like lower(concat('%', :searchTerm, '%'))")
	List<Product> findByProductName(String searchTerm);

}
