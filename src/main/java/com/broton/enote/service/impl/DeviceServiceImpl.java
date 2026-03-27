package com.broton.enote.service.impl;

import java.math.BigInteger;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.Device;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.BranchRepository;
import com.broton.enote.repository.DeviceRepository;
import com.broton.enote.service.DeviceService;
import com.broton.enote.utils.CommonUtil;
import com.broton.enote.utils.PageUtils;
import com.broton.enote.bo.AddDeviceInfoBO;
import com.broton.enote.bo.QueryDeviceStatusListBO;
import com.broton.enote.bo.SendDeviceInfoBO;
import com.broton.enote.dto.DeviceResultListDto;
import com.broton.enote.dto.SetDeviceInfoReturnDto;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private BranchRepository branchRepository;
	
	@Override
	public Page<DeviceResultListDto> getDeviceStatusList(QueryDeviceStatusListBO qryBo) {
		Page<DeviceResultListDto> pageDto = null;
		try {
			Pageable pageable = PageUtils.getPageByParameter(qryBo.getStart(), qryBo.getLength(), null);
			pageDto = deviceRepository.getDeviceStatusList(qryBo, pageable);
		} catch (Exception e) {
			log.error("取得設備狀態資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5950.getCode(), ResultCode.ERR_5950.getDesc());
		}
		return pageDto;
	}

	@SuppressWarnings("null")
	@Override
	public SetDeviceInfoReturnDto setDeviceInfo(SendDeviceInfoBO sendInfoBo) {
		SetDeviceInfoReturnDto output = new SetDeviceInfoReturnDto();
		BigInteger deviceNo = deviceRepository.getDeviceNo(sendInfoBo.getDeviceId());		
		try {
			if (null == deviceNo) {
				log.error("接收設備發送的設備狀態錯誤,不存在的設備ID:{}",sendInfoBo.getDeviceId());
				throw new ErrorCodeException(ResultCode.ERR_5953.getCode(), ResultCode.ERR_5953.getDesc());
			} else {
				Device device = deviceRepository.findById(deviceNo).orElse(null);
				//BeanUtils.copyProperties(sendInfoBo, device);
				CommonUtil.customizeCopyProperties(sendInfoBo, device);
				String[] ipArray = sendInfoBo.getIp().split("\\.");
				String currentIp = ipArray[0] + "." + ipArray[1] + "." + ipArray[2];
				// 依據ip動態調整平板所屬的據點
				BigInteger currentBranchId = branchRepository.getBranchIdByNetwork("%" + currentIp + "%");
				if (null != currentBranchId) {
					output.setBranchId(currentBranchId);
					device.setBranchId(currentBranchId);
				}
				device.setLastUpdateTime(new Date());
				device.setLastLoginUser(sendInfoBo.getLastUpdateUser());
				device.setLastLoginUserName(sendInfoBo.getLastUpdateUserName());
				deviceRepository.save(device);
				log.info("接收設備發送的設備狀態成功 deviceId:{}", sendInfoBo.getDeviceId());
			}			
		} catch (Exception e) {
			log.error("接收設備發送的設備狀態錯誤 deviceId:{},{}", sendInfoBo.getDeviceId(), e.toString());
			if (e instanceof ErrorCodeException) {
				throw new ErrorCodeException(ResultCode.ERR_5953.getCode(), ResultCode.ERR_5953.getDesc());
			} else {
				throw new ErrorCodeException(ResultCode.ERR_5951.getCode(), ResultCode.ERR_5951.getDesc());
			}
		}
		return output;
	}
	
	@Override
	public void addDevice(AddDeviceInfoBO addBo) {		
		BigInteger deviceNo = deviceRepository.getDeviceNo(addBo.getDeviceId());
		Device device = null;
		try {
			if (null == deviceNo) {
				device = new Device();
			} else {
				device = deviceRepository.findById(deviceNo).orElse(null);
			}
			CommonUtil.customizeCopyProperties(addBo, device);
			//device.setLastUpdateTime(new Date());
			deviceRepository.save(device);
			log.info("新增/修改設備成功 deviceId:{}", addBo.getDeviceId());
		} catch (Exception e) {
			log.error("新增/修改設備錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5952.getCode(), ResultCode.ERR_5952.getDesc());
		}
	}
}
