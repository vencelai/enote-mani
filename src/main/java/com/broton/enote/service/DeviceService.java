package com.broton.enote.service;

import org.springframework.data.domain.Page;
import com.broton.enote.bo.AddDeviceInfoBO;
import com.broton.enote.bo.QueryDeviceStatusListBO;
import com.broton.enote.bo.SendDeviceInfoBO;
import com.broton.enote.dto.DeviceResultListDto;
import com.broton.enote.dto.SetDeviceInfoReturnDto;

/**
 * 設備相關 Service
 * 
 */
public interface DeviceService {
	
	/** 取得設備狀態列表
	 * @return
	 */
	public Page<DeviceResultListDto> getDeviceStatusList(QueryDeviceStatusListBO qryBo);
	
	/** 接收設備發送的設備狀態
	 * @param sendInfoBo
	 */
	public SetDeviceInfoReturnDto setDeviceInfo(SendDeviceInfoBO sendInfoBo);
	
	/** 後台管理-新增/修改設備
	 * @param addBo
	 */
	public void addDevice(AddDeviceInfoBO addBo);

}
