package com.milklabs.playground.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.milklabs.playground.entity.authentication.Role;
import com.milklabs.playground.entity.authentication.User;

class UserDAOTest {

	@Test
	void findByUsername_admin_returnsUserWithAdminRole() {
		UserDAO dao = new UserDAO();

		User admin = dao.findByUsername("admin");

		assertNotNull(admin);
		assertEquals("admin", admin.getUsername());
		assertNotNull(admin.getRoles());
		assertTrue(admin.getRoles().contains(Role.ADMIN));
	}

	@Test
	void findByUsername_user_returnsUserWithUserRole() {
		UserDAO dao = new UserDAO();

		User user = dao.findByUsername("user");

		assertNotNull(user);
		assertEquals("user", user.getUsername());
		assertNotNull(user.getRoles());
		assertTrue(user.getRoles().contains(Role.USER));
	}

	@Test
	void findByUsername_unknown_returnsNull() {
		UserDAO dao = new UserDAO();

		assertNull(dao.findByUsername("does-not-exist"));
	}
}
