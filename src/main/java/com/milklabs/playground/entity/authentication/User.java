package com.milklabs.playground.entity.authentication;

import java.io.Serializable;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = 7837262168668140260L;
	private String username;
	private String name; 

	private String hashedPassword;
	private Set<Role> roles;
	private byte[] profilePicture;
	
	/**
	 * 
	 * @param username
	 * @param name
	 * @param hashedPassword
	 * @param roles
	 * @param profilePicture
	 */
	public User(String username, String name, String hashedPassword, Set<Role> roles,  byte[] profilePicture) {
		this.username = username;
		this.name = name;
		this.hashedPassword = hashedPassword;
		this.roles = roles;
		this.profilePicture = profilePicture;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n==========\nUser class\n").append("username: [")
				.append(username)
				.append("]\n name: [")
				.append(name)
				.append("]\n hashedPassword: [")
				.append(hashedPassword)
				.append("]\n roles: [")
				.append(roles)
				.append("]\n profilePicture: [")
				.append(profilePicture)
				.append("]\n==========");
		
		return sb.toString();
				
	}
	
}
