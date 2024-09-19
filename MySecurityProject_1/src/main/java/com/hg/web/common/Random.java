package com.hg.web.common;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;
@Component
public final class Random {

	private static final String lowercase="abcdefghijklmnopqrstuvwxyz";
	private static final String uppercase="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String degits="0123456789";
	private static final String special_chars="!@#$%^&*()_+-=[]{}|;:',.<>?";
	
	private static final String all_chars=lowercase+uppercase+degits+special_chars;

	private static final SecureRandom random=new SecureRandom();
	
	// 하나 이상의 알파벳, 숫자, 특수문자를 포함해야한다는 조건 만족
	public static String getRandomPassword() {
		StringBuilder password=new StringBuilder();
		password.append(getRandomChar(lowercase));
		password.append(getRandomChar(uppercase));
		password.append(getRandomChar(degits));
		password.append(getRandomChar(special_chars));
		
		// 나머지 12자리는 모든 문자 중 랜덤으로 발생
		for(int i=4;i<15;i++) {
			password.append(getRandomChar(all_chars));
		}
		
		// 배열 형태로 변환
		char[] passwordArray=password.toString().toCharArray(); 
		
		// 피셔 에이츠 알고리즘 => 배열 요소를 무작위로 섞어서 보안을 더욱 강화
		for(int i=passwordArray.length-1;i>0;i--) { // 총 15번의 교환이 일어남
			int index=random.nextInt(i+1); // 0~i까지 랜덤 난수 발생
			char temp=passwordArray[index]; // index 요소를 temp에 저장
			passwordArray[index]=passwordArray[i]; // index 위치에 i 요소 저장
			passwordArray[i]=temp; // i 위치에 index 요소 저장
		}
		System.out.println(new String(passwordArray));
		return new String(passwordArray);
		
	}
	
	// 하나의 랜덤 문자 발생
	private static char getRandomChar(String character) {
		int index=random.nextInt(character.length()); // 문자열 길이를 반환하여 "길이-1" 사이의 랜덤한 정수 발생
		return character.charAt(index); // 인덱스에 위치한 문자를 반환함
	}
}
