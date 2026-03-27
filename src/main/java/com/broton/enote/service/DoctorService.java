package com.broton.enote.service;

import java.util.List;
import com.broton.enote.bo.DoctorStfNoBO;
import com.broton.enote.dto.DoctorResultListDto;

/**
 * 醫師 Service
 * 
 */
public interface DoctorService {
	
	/** 取得醫師列表
	 * @return
	 */
	public List<DoctorResultListDto> getDoctorList();
	
	/** 取得醫師的印章 (png base64)
	 * @param id
	 * @return
	 */
	public String getDoctorStamp(DoctorStfNoBO idBo);

}
