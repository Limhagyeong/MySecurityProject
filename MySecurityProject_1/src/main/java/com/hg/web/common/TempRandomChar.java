package com.hg.web.common;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;
@Component
public final class TempRandomChar {

	private static final String LOWERCASE="abcdefghijklmnopqrstuvwxyz";
	private static final String UPPERCASE="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DEGITS="0123456789";
	private static final String SPECIAL_CHAR="!@#$%^&*()_+-=[]{}|;:',.<>?";
	
	private static final String ALL_CHAR=LOWERCASE+UPPERCASE+DEGITS+SPECIAL_CHAR;

	private static final SecureRandom RANDOM=new SecureRandom(); // 일반 랜덤 클래스보다 예측하기 어려운 난수 제공
	
	private static final int LENGTH=6; // 이메일 인증
	
	// 비밀번호는 하나 이상의 알파벳, 숫자, 특수문자를 포함해야한다는 조건 만족
	public static String getRandomPassword() {
		StringBuilder password=new StringBuilder();
		password.append(getRandomChar(LOWERCASE));
		password.append(getRandomChar(UPPERCASE));
		password.append(getRandomChar(DEGITS));
		password.append(getRandomChar(SPECIAL_CHAR));
		
		// 나머지 12자리는 모든 문자 중 랜덤으로 발생
		for(int i=4;i<15;i++) {
			password.append(getRandomChar(ALL_CHAR));
		}
		
		// 배열 형태로 변환
		char[] passwordArray=password.toString().toCharArray(); 
		
		// 피셔 에이츠 알고리즘 => 배열 요소를 무작위로 섞어서 보안을 더욱 강화
		for(int i=passwordArray.length-1;i>0;i--) { // 총 15번의 교환이 일어남
			int index=RANDOM.nextInt(i+1); // 0~i까지 랜덤 난수 발생
			char temp=passwordArray[index]; // index 요소를 temp에 저장
			passwordArray[index]=passwordArray[i]; // index 위치에 i 요소 저장
			passwordArray[i]=temp; // i 위치에 index 요소 저장
		}

		return new String(passwordArray);
	}
	
	// 하나의 랜덤 문자 발생
	private static char getRandomChar(String character) {
		int index=RANDOM.nextInt(character.length()); // 문자열 길이를 반환하여 "길이-1" 사이의 랜덤한 정수 발생
		return character.charAt(index); // 인덱스에 위치한 문자를 반환함
	}
	
	// 난수 이용 이메일 인증번호 랜덤 생성
    public static String emailAuthCode() {
        SecureRandom random = new SecureRandom(); // 일반 랜덤 클래스보다 예측하기 어려운 난수 제공
        
        //Math.pow(10, LENGTH) => 10의 6(LENGTH)제곱 => 0~999,999까지의 난수 생성
        int code = random.nextInt((int) Math.pow(10, LENGTH));
    
        //생성된 code값이 지정한 LENGTH보다 작다면 부족한 부분은 0으로 채워줌
        return String.format("%0" + LENGTH + "d", code);
    }
	
}
