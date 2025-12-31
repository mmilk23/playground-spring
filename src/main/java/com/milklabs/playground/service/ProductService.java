package com.milklabs.playground.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.milklabs.playground.dao.ProductDAO;
import com.milklabs.playground.entity.Product;

@Service
public class ProductService {

	private final ProductDAO productDAO;

	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	public ProductService(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public List<Product> findAll() {
		return StreamSupport.stream(productDAO.findAll().spliterator(), false).toList();
	}

	public List<Product> findAllProduct(String stringFilter) {
		log.debug("[findAllProduct] stringFilter: [{}]", stringFilter);

		if (stringFilter == null || stringFilter.isEmpty()) {
			return StreamSupport.stream(productDAO.findAll().spliterator(), false).toList();
		} else {
			return productDAO.findByProductName(stringFilter);
		}
	}

	public void deleteProduct(Product c) {
		productDAO.delete(c);
	}

	public void saveProduct(Product category) {
		if (category == null) {
			log.error("[saveProduct]  product null. Are you sure you have connected your form to the application?");
			return;
		}
		productDAO.save(category);
	}

}
