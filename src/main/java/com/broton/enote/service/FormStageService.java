package com.broton.enote.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.broton.enote.bo.FormStageIdBO;
import com.broton.enote.bo.PdfStageBO;
import com.broton.enote.bo.QueryFormStageListBO;
import com.broton.enote.bo.SetFormStageLockBO;
import com.broton.enote.dto.FormStageResultListDto;
import com.broton.enote.dto.PdfSignTrackDto;

/**
 * 表單暫存 Service
 * 
 */
public interface FormStageService {
	
	/**
	 * 取得表單暫存資料列表
	 * 
	 * @param qryBo
	 */
	public List<FormStageResultListDto> getFormStageList(QueryFormStageListBO qryBo);
	
	/**
	 * 取得表單暫存資料列表(分頁)
	 * 
	 * @param qryBo
	 */
	public Page<FormStageResultListDto> getFormStagePageList(QueryFormStageListBO qryBo);
	
	/** 由流水號取得表單暫存資料
	 * @param idBo
	 * @return
	 */
	public FormStageResultListDto getFormStageById(FormStageIdBO idBo);
	
	/** 新增/修改暫存表單
	 * @param bo
	 * @param employeeId
	 */
	public void formStageImport(PdfStageBO bo);
		
	/** 取得表單暫存 PDF base64, 簽名軌跡 base64
	 * @param qryBo
	 * @return PdfSignTrackDto
	 */
	public PdfSignTrackDto getFormStagePdfSignTrack(FormStageIdBO idBo);
	
	/** 表單暫存棄用
	 * 
	 */
	public void terminationFormStage(FormStageIdBO idBo);
	
	/** 解除表單暫存鎖定
	 * 
	 */
	public void abortFormStageLock(FormStageIdBO idBo);
	
	/** 設定表單暫存鎖定
	 * 
	 */
	public void setFormStageLock(SetFormStageLockBO bo);
	
	/** 上傳暫存表單到 erp
	 * @param idBo
	 */
	public void formStageUploadToErp(PdfStageBO bo);
	
	/** 刪除超過保留期限的表單暫存資料(只針對棄用的)
	 * 
	 */
	public void deleteExpiredFormStage();
	
}
