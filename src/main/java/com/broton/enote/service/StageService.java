package com.broton.enote.service;

import java.util.List;
import com.broton.enote.bo.CustomerIdNameBO;
import com.broton.enote.bo.EmployeeIdBO;
import com.broton.enote.bo.StageIdBO;
import com.broton.enote.bo.UploadStageBO;
import com.broton.enote.dto.StageDto;
import com.broton.enote.dto.StageGroupDto;
import com.broton.enote.dto.StageListDto;

/**
 * 表單暫存 Service
 * 
 */
public interface StageService {
	
	/** 上傳暫存表單(有id值表示為更新, 若無則表示為新增)
	 * @param updBo
	 */
	public void uploadStage(UploadStageBO uploadBo);
	
	/** 取得暫存表單群組by員工id(依客戶id,名稱做為 group 條件)
	 * @param idBo
	 * @return
	 */
	public List<StageGroupDto> getStageGroup(EmployeeIdBO idBo);
	
	/** 依據消費者id,名稱作為搜尋條件,取得暂存表單清單
	 * @param nameBo
	 * @return
	 */
	public List<StageListDto> getStageGroupList(CustomerIdNameBO idNameBo);
	
	/** 依據暫存表單id取得暂存表單資料
	 * @param idBo
	 * @return
	 */
	public StageDto getStageById(StageIdBO idBo);
}
