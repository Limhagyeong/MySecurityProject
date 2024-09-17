package com.hg.web.common;

import java.util.regex.Pattern;
// 입력값에 대한 1차 유효성 검사는 프론트에서 진행하고
// 2차적으로 서버에서 한번 더 막음 

public final class InputValidator {

	// IDValidation
	public static boolean IDValCheck(String value) {
		// 값이 존재하지 않는다면
		if(!notNull(value)) {
			return false;
		}
		// 공백으로 시작한다면
		if(!notBlankStart(value)) {
			return false;
		}
		// 공백을 포핳한다면
		if(!notContainsBlank(value)) {
			return false;
		}
		// 한글을 포함한다면
		if(!notContainsKor(value)) {
			return false;
		}
		// 모든 검사를 통과함
		return true;
	}
	
	// 값이 존재해야함
	public static boolean notNull(String value) {
		if(value==null) {
			return false;
		}
		return true;
	}
	
	// 공백으로 시작할 수 없음
	public static boolean notBlankStart(String value) {
		if(value.startsWith(" ")) {
			return false;
		}
		return true;
	}
	
	// 공백을 포함할 수 없음
	public static boolean notContainsBlank(String value) {
		
		Pattern containsBlank=Pattern.compile("\\s");
		
		if(containsBlank.matcher(value).find()) {
			return false;
		}
		return true;
	}
	
	// 영문자, 숫자만 허용 (한글x)
	public static boolean notContainsKor(String value) {
		Pattern containingKor=Pattern.compile("[ㄱ-ㅎㅏ-ㅣ가-힣]");
		
		if(containingKor.matcher(value).find()) {
			return false;
		}
		return true;
	}

	// 
	
}
