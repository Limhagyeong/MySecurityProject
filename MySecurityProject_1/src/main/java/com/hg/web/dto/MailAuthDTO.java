package com.hg.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MailAuthDTO {

	private int mailauthcode_num;
	private String code;
	private LocalDateTime expiration;
	private String isauth;
	private String email;
	
}
