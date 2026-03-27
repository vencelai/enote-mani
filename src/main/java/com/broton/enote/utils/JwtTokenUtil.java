package com.broton.enote.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenUtil implements Serializable {

private static final long serialVersionUID = -2550185165626007488L;
	
	private static final long JWT_TOKEN_VALIDITY = 12*60*60; // 12小時
	private static final long PROJECT_TOKEN_VALIDITY = 30;//30秒

	private static String secret;
	
	@Value("${jwt.secret}")
	private void setSecret(String secretKey) {
		secret = secretKey;
	}

	public static String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public static List<GrantedAuthority> getRolesFromToken(String token){
		Claims claims = getAllClaimsFromToken(token);
		return AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("roles"));
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private static Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	public static String getJtiFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		// 塞入所屬權限
		//claims.put("roles", "ADMIN,GUEST");
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setId(CommonUtil.generateUUID()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	/**
	 * 產生專案令牌
	 * 
	 * @param username
	 * @param clientId
	 * @return String
	 */
	public static String generateProjectToken(String username,String clientId) {
		Claims claims = Jwts.claims()
				.setSubject(username).setId(CommonUtil.generateUUID());
		claims.put("clientId", clientId);	
		return createProjectToken(claims);
	}
	
	private static String createProjectToken(Claims claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + PROJECT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public Authentication getAuthentication(HttpServletRequest request) {
    	
        // 從request的header拿回token
        String token = request.getHeader("Authorization");

        if (token != null) {
            // 解析 Token
        	try {
        		Claims claims = Jwts.parser()
                        // 驗證
                        .setSigningKey(secret)
                        // 去掉token 開頭 Bearer
                        .parseClaimsJws(token.replace("Bearer", ""))
                        .getBody();

                // 拿用户名
                String user = claims.getSubject();
                //String roles = (String) claims.get("roles");
                //System.out.println("roles:" + roles);

                // 得到權限
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("roles"));
                // 返回Token
                return user != null ? new UsernamePasswordAuthenticationToken(user, null, authorities) : null;
        	} catch (JwtException ex) {
        		log.error(ex);
        	}
            
        }
        return null;
    }  
}
