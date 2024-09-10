package com.hg.web.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class UserDTO {
	private int id;
	
	private String username;
	
	private String pwd;
	
	private String name;
	
	private String gender;
	
	private Date bday;
	
	private String phone;
	
	private String email;
	
	private Date joindate;
	
	private String role;
	
}
