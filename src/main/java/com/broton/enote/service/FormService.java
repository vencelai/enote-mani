package com.broton.enote.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.broton.enote.bo.FormAddBO;
import com.broton.enote.bo.FormIdBO;
import com.broton.enote.bo.FormInputAddBO;
import com.broton.enote.bo.FormInputIdBO;
import com.broton.enote.bo.FormInputUpdateBO;
import com.broton.enote.bo.FormUpdateBO;
import com.broton.enote.bo.QueryFormListAllBO;
import com.broton.enote.bo.QueryFormListBO;
import com.broton.enote.bo.SetFormShowBO;
import com.broton.enote.dto.FormInputListResultDto;
import com.broton.enote.dto.FormListResultDto;
import com.broton.enote.dto.FormPdfDto;

/**
 * 表單資料 Service
 * 
 */
public interface FormService {
	
	/**
	 * 取得表單資料列表
	 * 
	 * @param qryBo
	 */
	public Page<FormListResultDto> getFormList(QueryFormListBO qryBo);
	
	/**
	 * 取得表單資料列表
	 * 
	 * @param qryBo
	 */
	public List<FormListResultDto> getFormListAll(QueryFormListAllBO qryBo);
	
	/** 取得表單 PDF base64
	 * @param qryBo
	 * @return
	 */
	public FormPdfDto getFormPdf(FormIdBO qryBo);
	
	/** 取得表單控制項列表
	 * @param qryBo
	 * @return
	 */
	public List<FormInputListResultDto> getFormInputList(FormIdBO qryBo);
	
	/** 新增表單
	 * @param addBo
	 * @param loginUser
	 */
	public void formAdd(FormAddBO addBo, String loginUser);
	
	/** 更新表單
	 * @param updBo
	 * @param loginUser
	 */
	public void formUpdate(FormUpdateBO updBo, String loginUser);
	
	/** 刪除表單
	 * @param idBo
	 */
	public void formDelete(FormIdBO idBo);
	
	/** 表單上/下架
	 * @param setBo
	 */
	public void setFormShow(SetFormShowBO setBo, String loginUser);
	
	/** 新增表單輸入項
	 * @param addBo
	 * @param loginUser
	 */
	public void formInputAdd(FormInputAddBO addBo, String loginUser);
	
	/** 更新表單輸入項
	 * @param updBo
	 * @param loginUser
	 */
	public void formInputUpdate(FormInputUpdateBO updBo, String loginUser);
	
	/** 刪除表單輸入項
	 * @param idBo
	 */
	public void formInputDelete(FormInputIdBO idBo);

}
