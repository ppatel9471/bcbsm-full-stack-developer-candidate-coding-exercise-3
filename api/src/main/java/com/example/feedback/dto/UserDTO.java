package com.example.feedback.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserDTO {
	private String name;
	private String username;
	private String password;
	private Boolean isAdmin;
	
	public Boolean isAdmin() {
		return this.isAdmin;
	}
}
