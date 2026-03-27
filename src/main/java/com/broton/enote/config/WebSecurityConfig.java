package com.broton.enote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.broton.enote.security.JwtRequestProvider;
import com.broton.enote.security.JwtAuthenticationEntryPoint;
import com.broton.enote.security.JwtRequestFilter;
import com.broton.enote.security.JwtRequestSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                               "/configuration/ui",
                               "/swagger-resources/**",
                               "/configuration/security",
                               "/swagger-ui.html",
                               "/api/v1/manager/managerLogin",
                               "/api/v1/manager/forgetPasswordBySMS",
                               "/api/v1/manager/token",
                               "/pad/api/**",
                               "/api/v1/test/**",                               
                               "/webjars/**",
                               "/actuator/info");
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(getJwtRequestProvider());
    }
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// 允許跨域訪問
		httpSecurity.cors()
			.and().csrf().disable()
			.authorizeRequests().// 允許靜態資源訪問
	        antMatchers("/consumerRegister").permitAll()
	        .anyRequest().authenticated()
	        .and().exceptionHandling()
	        	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
	        .and().sessionManagement()
	        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and().addFilterBefore(getJwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	private JwtRequestProvider getJwtRequestProvider() {
		return new JwtRequestProvider();
	}
	
	private JwtRequestFilter getJwtRequestFilter() throws Exception {
		JwtRequestFilter jwtRequestFilter = new JwtRequestFilter("/api/**");
		jwtRequestFilter.setAuthenticationManager(authenticationManager());
		jwtRequestFilter.setAuthenticationSuccessHandler(new JwtRequestSuccessHandler());
		return jwtRequestFilter;
	}

}
