/**
 * 
 */
package com.broton.enote.security;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.broton.enote.dto.common.JwtRequestToken;
import com.broton.enote.exception.TokenException;
import com.broton.enote.utils.JwtTokenUtil;

import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;

/**
 * @title JWT請求提供者
 * @desction JWT請求提供者
 * @author RexKu
 */
@Log4j2
public class JwtRequestProvider extends AbstractUserDetailsAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		log.debug("JwtAuthenticationProvider.additionalAuthenticationChecks()");
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		JwtRequestToken jwtRequestToken = (JwtRequestToken) authentication;
		String token = jwtRequestToken.getToken();
		if("".equals(token) || token == null) {
			throw new TokenException(HttpStatus.FORBIDDEN,"token is not fount");
		}
		UserDetails user = null;
		try {
			List<GrantedAuthority> authorities = JwtTokenUtil.getRolesFromToken(token);
			user = new User(username, "N/A", authorities);
		}catch (SignatureException je) {
			throw new TokenException(HttpStatus.NOT_ACCEPTABLE,"token is wrong");
		}
		return user;
	}
	
}
