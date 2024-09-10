package com.hg.web.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class SecSessionServiceImpl {
	
	public Map<String,String> secSession() {
		
		String id=SecurityContextHolder.getContext().getAuthentication().getName(); // 스프링 세션 아이디
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iter = authorities.iterator();
		GrantedAuthority auth = iter.next();
		String role = auth.getAuthority(); // 스프링 세션 롤
		
		Map<String,String> map=new HashMap<>();
		
		map.put("id", id);
		map.put("role",role);
		
		return map;
	}
}
