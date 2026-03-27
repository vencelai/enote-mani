package com.broton.enote.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JwtRequestSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.debug("JwtAuthenticationSuccessHandler.onAuthenticationSuccess()...");
	    // 因為預設的AuthenticationSuccessHandler通過驗證後會重新導向到發出請求時的頁面, 所以這邊要複寫掉不做任何事
	}

}
