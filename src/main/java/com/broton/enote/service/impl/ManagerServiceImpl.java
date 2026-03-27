package com.broton.enote.service.impl;

import java.math.BigInteger;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.broton.enote.bo.ManagerBO;
import com.broton.enote.bo.ManagerIdBO;
import com.broton.enote.bo.QueryManagerBO;
import com.broton.enote.bo.UpdateManagerBO;
import com.broton.enote.dto.ManagerResultDto;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.Manager;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.ManagerRepository;
import com.broton.enote.service.ManagerService;
import com.broton.enote.utils.PageUtils;
import com.broton.enote.utils.JwtTokenUtil;
import com.broton.enote.bo.LoginBO;
import com.broton.enote.dto.LoginReturnDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	@Override
	public LoginReturnDto managerLogin(LoginBO loginBO) {
		LoginReturnDto output = new LoginReturnDto(); 
		final UserDetails userDetails2 = jwtInMemoryUserDetailsService.loadUserByUsername(loginBO.getId() + "#bt#" + loginBO.getPassword());
		if (null != userDetails2) {
			if ("unActive".equals(userDetails2.getUsername())) {
				log.error("此管理人員為停用狀態:{}", loginBO.getId());
				throw new ErrorCodeException(ResultCode.ERR_5303.getCode(), ResultCode.ERR_5303.getDesc());
			} else {
				BigInteger uuid = managerRepository.getManagerById(userDetails2.getUsername());
				Manager manager = managerRepository.findById(uuid).orElse(null);
				String genToken = jwtTokenUtil.generateToken(userDetails2);
				//output.setId(manager.getUserId());
				output.setName(manager.getUserName());
				output.setId(manager.getId());
				output.setUserType("manager");
				output.setToken("Bearer " + genToken);
				log.info("管理人員登入檢核成功 {}", output);
			}
		} else {
			log.error("登入檢核失敗:{}", loginBO);
			throw new ErrorCodeException(ResultCode.ERR_5302.getCode(), ResultCode.ERR_5302.getDesc());
		}	
		return output;
	}

	@Override
	public Page<ManagerResultDto> getManagerList(QueryManagerBO queryManagerBO) {
		Page<ManagerResultDto> pageDto = null;
		try {
			Pageable pageable = PageUtils.getPageByParameter(queryManagerBO.getStart(), queryManagerBO.getLength(), null);
			pageDto = managerRepository.getManagerList(queryManagerBO.getUserId(), queryManagerBO.getUserName(), pageable);
		} catch (Exception e) {
			log.error("取得管理人員資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5501.getCode(), ResultCode.ERR_5501.getDesc());
		}
		return pageDto;
	}

	@Override
	public void addManager(ManagerBO managerBO, String maintainer) {
		try {
			int cnt = managerRepository.getManagerIdCount(managerBO.getUserId());
			if (0 == cnt) {
				Manager manager = new Manager();
				manager.setUserId(managerBO.getUserId());
				manager.setUserName(managerBO.getUserName());
				manager.setUserPassword(managerBO.getPassword());
				manager.setActive(managerBO.getActive());
				manager.setCreateUser(maintainer);
				manager.setCreateDate(new Date());
				managerRepository.save(manager);
				log.info("新增管理人員成功 {}", manager);
			} else {
				log.error("新增管理人員錯誤,帳號重複:{}", managerBO.getUserId());
				throw new ErrorCodeException(ResultCode.ERR_5502.getCode(), ResultCode.ERR_5502.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("新增管理人員錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5503.getCode(), ResultCode.ERR_5503.getDesc());
		}
	}

	@Override
	public void delManager(ManagerIdBO managerIdBO) {
		try {
			if (managerRepository.existsById(managerIdBO.getId())) {
				Manager manager = managerRepository.findById(managerIdBO.getId()).orElse(null);
				if (null != manager) {
					if (!"ADMIN".equals(manager.getUserId().toUpperCase())) {
						managerRepository.deleteById(managerIdBO.getId());
						log.info("刪除管理人員成功:{}", managerIdBO.getId());
					}
				}	
			} else {
				log.error("刪除管理人員錯誤,管理人員資料不存在:{}", managerIdBO.getId());
				throw new ErrorCodeException(ResultCode.ERR_5504.getCode(), ResultCode.ERR_5504.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("刪除管理人員錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5505.getCode(), ResultCode.ERR_5505.getDesc());
		}
	}

	@Override
	public void updateManager(UpdateManagerBO updateManagerBO, String maintainer) {
		try {
			if (managerRepository.existsById(updateManagerBO.getId())) {
				Manager manager = managerRepository.findById(updateManagerBO.getId()).orElse(null);
				if (null != updateManagerBO.getUserName() && StringUtils.hasText(updateManagerBO.getUserName())) {
					manager.setUserName(updateManagerBO.getUserName());
				}
				if (null != updateManagerBO.getUserPassword() && StringUtils.hasText(updateManagerBO.getUserPassword())) {
					manager.setUserPassword(updateManagerBO.getUserPassword());
				}
				if (null != updateManagerBO.getActive()) {
					manager.setActive(updateManagerBO.getActive());
				}
				manager.setEditUser(maintainer);
				manager.setEditDate(new Date());
				managerRepository.save(manager);
				log.info("修改管理人員成功 {}", updateManagerBO);
			} else {
				log.error("修改管理人員錯誤,會員資料不存在:{}", updateManagerBO.getId());
				throw new ErrorCodeException(ResultCode.ERR_5506.getCode(), ResultCode.ERR_5506.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("修改管理人員錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5507.getCode(), ResultCode.ERR_5507.getDesc());
		}
	}

}
