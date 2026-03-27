package com.broton.enote.service;

import org.springframework.data.domain.Page;
import com.broton.enote.bo.ManagerBO;
import com.broton.enote.bo.ManagerIdBO;
import com.broton.enote.bo.QueryManagerBO;
import com.broton.enote.bo.UpdateManagerBO;
import com.broton.enote.dto.ManagerResultDto;
import com.broton.enote.bo.LoginBO;
import com.broton.enote.dto.LoginReturnDto;

/**
 * 管理人員 Service
 * 
 */
public interface ManagerService {
	
	/**
	 * 管理人員登入
	 * 
	 * @param loginBO
	 */
	public LoginReturnDto managerLogin(LoginBO loginBO);

	/**
	 * 取得管理人員資料列表
	 * 
	 * @param queryManagerBO
	 */
	public Page<ManagerResultDto> getManagerList(QueryManagerBO queryManagerBO);

	/**
	 * 新增管理人員
	 * 
	 * @param managerBO
	 * @param maintainer
	 */
	public void addManager(ManagerBO managerBO, String maintainer);

	/**
	 * 刪除管理人員
	 * 
	 * @param managerIdBO
	 */
	public void delManager(ManagerIdBO managerIdBO);

	/**
	 * 修改管理人員
	 * 
	 * @param updateManagerBO
	 * @param maintainer
	 */
	public void updateManager(UpdateManagerBO updateManagerBO, String maintainer);

}
