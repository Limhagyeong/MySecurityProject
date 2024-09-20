package com.hg.web.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


public class CustomUserDetails implements UserDetails{
	
	UsernamePasswordAuthenticationFilter up=new UsernamePasswordAuthenticationFilter();
	
	private final UserDTO dto;
	
	public CustomUserDetails(UserDTO dto) {
		this.dto=dto;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
            	System.out.println("dto롤"+dto.getRole());
                return dto.getRole();
            }
        });
        return collection;
	} // 권한 검증

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return dto.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return dto.getUsername();
		
	}
}
