package com.milklabs.playground.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.milklabs.playground.entity.Category;

@DataJpaTest(properties = {
		// use schema.sql + data.sql (não use Hibernate DDL aqui)
		"spring.jpa.hibernate.ddl-auto=none", "spring.sql.init.mode=always",
		"spring.sql.init.schema-locations=classpath:schema.sql", "spring.sql.init.data-locations=classpath:data.sql",
		// Derby in-memory igual ao app
		"spring.datasource.url=jdbc:derby:memory:playgrounddb;create=true",
		"spring.datasource.driver-class-name=org.apache.derby.jdbc.EmbeddedDriver",
		"spring.datasource.username=playground_spring", "spring.datasource.password=passw0rd" })
class CategoryDAOTest {

	@Autowired
	private CategoryDAO categoryDAO;

	@Test
	void findByCategoryName_shouldReturnElectronicsAndItsSubcategories_whenSearchElect() {
		// "Electronics" + "Mobile Phones" + "Laptops" contêm "elect"?
		// Na verdade só "Electronics" contém "elect". Vamos buscar "lap" para ser
		// único.
		List<Category> results = categoryDAO.findByCategoryName("lap");

		assertEquals(1, results.size());
		assertEquals("Laptops", results.get(0).getCategoryName());
	}

	@Test
	void findByCategoryId_shouldReturnBooks_whenIdIs2() {
		Category c = categoryDAO.findByCategoryId(2);

		assertNotNull(c);
		assertEquals("Books", c.getCategoryName());
		assertNull(c.getCategoryParent());
	}

	@Test
	void findAllPrecedingCategories_shouldReturnParentAndChildren_whenCategoryIdIs1() {
		// Query do DAO:
		// SELECT c FROM Category c WHERE c.categoryId = :categoryId OR
		// c.categoryParent.categoryId = :categoryId
		List<Category> results = categoryDAO.findAllPrecedingCategories(1);

		Set<String> names = results.stream().map(Category::getCategoryName).collect(Collectors.toSet());

		assertEquals(3, results.size());
		assertTrue(names.contains("Electronics"));
		assertTrue(names.contains("Mobile Phones"));
		assertTrue(names.contains("Laptops"));

		// não deve incluir categorias de Books
		assertFalse(names.contains("Fiction"));
		assertFalse(names.contains("Non-Fiction"));
	}

	@Test
	void findAll_shouldReturn8Categories_fromDataSql() {
		// 3 principais + 4 sub + 1 "Clothers" = 7? Vamos contar:
		// Electronics, Books, Clothers (3)
		// Mobile Phones, Laptops, Fiction, Non-Fiction (4)
		// Total = 7
		long count = categoryDAO.count();
		assertEquals(7, count);
	}
}
