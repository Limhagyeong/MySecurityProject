package com.hg.web.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hg.web.mapper.MailAuthMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailAuthCleanupScheduler {
	private final MailAuthMapper mailauthmapper;
	
	//매일 자정 이메일 미인증 이력 삭제 스케줄러
	@Scheduled(cron="0 0 * * * ?")
	public void cleanupExpiredCode() {
		try {
            int deletedCount = mailauthmapper.deleteMailAuthCode(); // 삭제된 레코드 수
            System.out.println(deletedCount + "개의 만료된 인증 코드가 삭제되었습니다.");
        } catch (Exception e) {
            System.err.println("이메일 인증 코드 삭제 중 오류 발생: " + e.getMessage());
        }
	}
}
