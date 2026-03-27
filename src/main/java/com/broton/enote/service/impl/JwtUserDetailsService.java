package com.broton.enote.service.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.broton.enote.entity.Manager;
import com.broton.enote.repository.ManagerRepository;
import lombok.extern.log4j.Log4j2;

/**
 * 管理者身份驗證
 */
@Service
@Log4j2
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	@Lazy
	PasswordEncoder passwordEncoder;
	@Autowired
	private ManagerRepository managerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String uid, pwd;
		List<String> items = Arrays.asList(username.split("#bt#"));
		uid = items.get(0);
		pwd = items.get(1);
		UserDetails userDetails = null;
		// 資料庫驗證
		log.debug("check by manager JDBC");		
		BigInteger uuid = managerRepository.managerLoginCheck(uid, pwd);
		if (null != uuid) {
			Manager manager = managerRepository.findById(uuid).orElse(null);
			if (null != manager) {
				if (manager.getActive() == 0) {
					userDetails = User.builder().username("unActive").password("N/A").authorities("N/A").build();
					log.info("帳號 {} 已被停用", manager.getUserId());
					return userDetails;
				} else {
					userDetails = User.builder()
							.username(uid)
							.password("N/A")
							// 密碼前面加上"{noop}"使用NoOpPasswordEncoder，也就是不對密碼進行任何格式的編碼。
							//.password("{noop}" + manager)
							// .roles("ADMIN", "USER")
							.authorities("manager")
							.build();
				}
			} else {
				log.error("check by manager JDBC search result null");
			}
		}

		return userDetails;
	}
}