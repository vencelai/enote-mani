package com.broton.enote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {	
	@Bean
	public CorsFilter corsFilter() {
		// 1.添加CORS配置資訊
		CorsConfiguration config = new CorsConfiguration();
		// 放行哪些原始域
//		config.addAllowedOriginPattern("*");
		// 是否發送Cookie資訊
		config.setAllowCredentials(true);
		// 放行哪些原始域(請求方式)
		config.addAllowedMethod("*");
		// 放行哪些原始域(頭部資訊)
		config.addAllowedHeader("*");
		// 暴露哪些頭部資訊（因為跨域訪問預設不能獲取全部頭部資訊）
		// config.addExposedHeader("*"); //vue用*會有問題，需要具體寫header訊息
		config.addExposedHeader("Content-Type");
		config.addExposedHeader("X-Requested-With");
		config.addExposedHeader("accept");
		config.addExposedHeader("Origin");
		config.addExposedHeader("Access-Control-Request-Method");
		config.addExposedHeader("Access-Control-Request-Headers");
		config.addExposedHeader("Authorization");
		// 2.添加映射路徑
		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
		configSource.registerCorsConfiguration("/**", config);
		// 3.返回新的CorsFilter.
		return new CorsFilter(configSource);
	}

}
