package com.broton.enote.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.broton.enote.dto.common.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @title JwtAuthenticationEntryPoint
 * @description 捕捉 Spring security 的 Unauthorized 錯誤,回傳統一格式的錯誤訊息
 * @author RexKu
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

		String code = "4001";
		String msg = "Unauthorized 不是合法的使用者";
		@SuppressWarnings("rawtypes")
		ResponseDto result = ResponseDto.builder().code(code).msg(msg).data(null).build();

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(result);
		out.print(json);
		out.flush();
	}
}
