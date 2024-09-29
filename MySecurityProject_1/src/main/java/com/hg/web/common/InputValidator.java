package com.hg.web.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
// 입력값에 대한 1차 유효성 검사는 프론트에서 진행하고
// 2차적으로 서버에서 한번 더 막음 

public final class InputValidator {

	// IDValidation
	public static boolean IDValCheck(String value) {
		// 값이 존재하고 + 공백으로 시작하지 않으며 + 공백을 포함하지 않는지
		if(!validateInput(value)) {
			return false;
		}
		// 한글 포함 X
		if(!notContainsKor(value)) {
			return false;
		}
		// 모든 검사를 통과함
		return true;
	}
	
	// pwdValidation
	public static boolean pwdValCheck(String value) {
		if(!validateInput(value)) {
			return false;
		}
		if(!leangthVal(value, 8, 20)) {
			return false;
		}
		if(!containSpecialChar(value)) {
			return false;
		}
		if(!ContainAlphabet(value)) {
			return false;
		}
		if(!ContainNum(value)) {
			return false;
		}
		
		return true;
	}
	
	// emailValidation
	public static boolean emailValCheck(String value) {
		// 값이 존재하고 + 공백으로 시작하지 않으며 + 공백을 포함하지 않는지
		if(!validateInput(value)) {
			return false;
		}
		// 이메일 형식인지
		if(!isEmailPattern(value)) {
			return false;
		}
		
		return true;
		
	}
	
	// phoneValidation
	public static boolean phoneValCheck(String value) {
		// 값이 존재하고 + 공백으로 시작하지 않으며 + 공백을 포함하지 않는지
		if (!validateInput(value)) {
			return false;
		}
		// 특수문자를 포함하지 않는지
		if(containSpecialChar(value)) {
			return false;
		}
		// 11자리인지
		if(!lengthLimit(value, 11)) {
			return false;
		}
		
		return true;
	}
	
	
	// nameValidation
	public static boolean nameValidation(String value) {
		// 값이 존재하고 + 공백으로 시작하지 않으며 + 공백을 포함하지 않는지
		if (!validateInput(value)) {
			return false;
		}
		// 특수문자를 포함하지 않는지
		if (containSpecialChar(value)) {
			return false;
		}
		// 숫자를 포함하지 않는지
		if(ContainNum(value)) {
			return false;
		}
		return true;
		
	}
	public static boolean validateInput(String value) {
		// 값이 존재해야함
		if(value==null || value.isEmpty()) {
			return false;
		}
		// 공백으로 시작할 수 없음
		if(value.startsWith(" ")) {
			return false;
		}
		// 공백을 포함할 수 없음
		Pattern containsBlank=Pattern.compile("\\s");
		
		if(containsBlank.matcher(value).find()) {
			return false;
		}
		return true;
	}
	
	
	// 한글을 포함하지 않는지
	public static boolean notContainsKor(String value) {
		Pattern containingKor=Pattern.compile("[ㄱ-ㅎㅏ-ㅣ가-힣]");
		
		if(containingKor.matcher(value).find()) {
			return false;
		}
		
		return true;
	}

	// 이메일 형식 
	public static boolean isEmailPattern(String value) {
		Pattern emailPattern=Pattern.compile(".+@.+\\..+");

		if(!emailPattern.matcher(value).find()) {
			return false;
		}
		
		return true;
	}
	
	// 입력 문자 길이 지정
	public static boolean lengthLimit(String value, int length) {
		if(value.length()!=length) {
			return false;
		}
		return true;
	}
	
	
	// 입력 문자 길이 검증
	public static boolean leangthVal(String value,int minLength, int maxLength) {
		if(value.length()<minLength || value.length()>maxLength) {
			return false;
		}
		return true;
	}
	
	// 하나 이상의 특수문자를 포함 하는지
	public static boolean containSpecialChar(String value) {
		Pattern specialChar=Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
		if(!specialChar.matcher(value).find()) {
			return false;
		}
		return true;
	}
	
	// 하나 이상의 영문자를 포함하는지
	public static boolean ContainAlphabet(String value) {

		Pattern alphabet = Pattern.compile(".*[a-zA-Z].*");
		if(!alphabet.matcher(value).find()) {
			return false;
		}
		return true;
	}
	
	// 하나 이상의 숫자를 포함하는지
	public static boolean ContainNum(String value) {

		Pattern num = Pattern.compile(".*\\d.*");
		if(!num.matcher(value).find()) {
			return false;
		}
		return true;
	}
	
	// 생년월일 패턴 검증
	public static boolean isDatePattern(String value) {
		
		Pattern datePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
		if (!datePattern.matcher(value).matches()) {
			return false;
		}
		 
		return true;
	}
	
	
	
}
