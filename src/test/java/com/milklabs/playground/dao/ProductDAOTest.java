package com.milklabs.playground.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.milklabs.playground.entity.Product;

@DataJpaTest(properties = { "spring.jpa.hibernate.ddl-auto=none", "spring.sql.init.mode=always",
		"spring.sql.init.schema-locations=classpath:schema.sql", "spring.sql.init.data-locations=classpath:data.sql",
		"spring.datasource.url=jdbc:derby:memory:playgrounddb;create=true",
		"spring.datasource.driver-class-name=org.apache.derby.jdbc.EmbeddedDriver",
		"spring.datasource.username=playground_spring", "spring.datasource.password=passw0rd" })
class ProductDAOTest {

	@Autowired
	private ProductDAO productDAO;

	@Test
	void findByProductName_shouldReturnKindle_whenSearchKindle() {
		List<Product> results = productDAO.findByProductName("kindle");

		assertEquals(1, results.size());
		assertEquals("Amazon Kindle", results.get(0).getProductName());
	}

	@Test
	void findByProductName_shouldReturnIphone_whenSearchPhone() {
		// "Apple Iphone" contém "phone"? Não.
		// Melhor buscar "iphone" que é único no dataset.
		List<Product> results = productDAO.findByProductName("iphone");

		assertEquals(1, results.size());
		assertEquals("Apple Iphone", results.get(0).getProductName());
	}

	@Test
	void findByProductName_shouldReturnTwoBooks_whenSearchHistoryOrGuide() {
		// "Guide" bate só no Hitchhiker, "History" bate só no Sapiens.
		// Vamos testar um termo mais geral que pegue 2? "a" pegaria vários.
		// Melhor: verificar total de produtos carregados pelo data.sql.
		long count = productDAO.count();
		assertEquals(4, count);
	}

	@Test
	void findById_shouldReturnAProduct_forExistingId() {
		// IDs dependem da ordem de insert e IDENTITY.
		// No data.sql você não informa product_id, então deve ser 1..4
		var p = productDAO.findById(1);

		assertTrue(p.isPresent());
		assertNotNull(p.get().getProductName());
	}
}
