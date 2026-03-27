package com.broton.enote.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.broton.enote.dto.common.JwtRequestToken;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.exception.TokenException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;

/**
 * @title  檢查 Token 的過濾器
 * @desction 檢查 Token 的過濾器
 * @author RexKu
 */
@Log4j2
public class JwtRequestFilter extends AbstractAuthenticationProcessingFilter {
	
	public JwtRequestFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("JwtAuthenticationFilter.attemptAuthentication()" + request.getRequestURI());
		String header = request.getHeader("Authorization");
		if(header == null || !header.startsWith("Bearer ")) {
			throw new TokenException(HttpStatus.UNAUTHORIZED, "token is missing part of words or there is empty");
		}
		String authToken = header.substring(7);
		JwtRequestToken token = null;
		try {
			token = new JwtRequestToken(authToken);
		} catch (ExpiredJwtException expired) {
			throw new TokenException(HttpStatus.UNAUTHORIZED,"jwt is expired");
		} catch (SignatureException | MalformedJwtException e) {
			throw new TokenException(HttpStatus.NOT_ACCEPTABLE,"token is wrong");
		}
		return getAuthenticationManager().authenticate(token);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		log.debug("Jwt Authentication success");
		
		chain.doFilter(request, response); // 驗證成功直接通過Filter導向資源位置
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		log.info("Jwt Authentication unsuccess: " + failed.getMessage());
		
		String code = "4001";
		String msg = "Unauthorized 不是合法的使用者";
		ResponseDto<Object> result = ResponseDto.builder().code(code).msg(msg).data(null).build();
			
        response.setContentType("application/json,charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    	PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        out.println(json);
        out.flush(); 
	}

}
