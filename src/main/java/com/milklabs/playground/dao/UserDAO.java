package com.milklabs.playground.dao;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

import com.milklabs.playground.entity.authentication.Role;
import com.milklabs.playground.entity.authentication.User;

@ApplicationScope
@Configuration
public class UserDAO {
	
	public UserDAO() {
		userList= Arrays.asList(new User("admin", "Adminstrator", "admin", new HashSet<>(Arrays.asList(Role.ADMIN)), new byte[0]), 
				 new User("user", "User", "admin", new HashSet<>(Arrays.asList(Role.USER)),  new byte[0]));
		
	}
	
	private static List<User> userList;


    public User findByUsername(String username) {    	
    	Optional<User> result = userList.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    	if (result.isPresent()) {
          return result.get();
        } else {
           return null;
        }
	}
}
