package com.hg.web.dto;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
	private int id;
	
	@NotBlank(message = "공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "공백으로 시작하거나 공백을 포함할 수 없음")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "영문자, 숫자, _ 만 허용")
	private String username; // ID
	
	@NotBlank(message = "공백일 수 없습니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자리 이상, 20자리 이하")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "공백으로 시작하거나 공백을 포함할 수 없음")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).*$", message = "문자,숫자,특수문자 하나 이상 포함")
	private String pwd;
	
	@NotBlank(message = "공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "공백으로 시작하거나 공백을 포함할 수 없음")
	private String name; // 실명
	
	private String gender;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date bday;
	
	@Pattern(regexp = "^\\d{11}$", message = "핸드폰번호는 11자리 정수 ")
	private String phone;
	
	@NotBlank(message = "공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "공백으로 시작하거나 공백을 포함할 수 없음")
    @Email(message = "이메일 형식이여야 합니다.")
	private String email;
	
	private Date joindate;
	
	private String role;
}
