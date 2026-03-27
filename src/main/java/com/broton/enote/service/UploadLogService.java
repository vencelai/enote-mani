package com.broton.enote.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.broton.enote.bo.QueryUploadLogListBO;
import com.broton.enote.bo.UploadLogIdBO;
import com.broton.enote.dto.UploadLogResultListDto;

/**
 * 上傳日誌 Service
 * 
 */
public interface UploadLogService {
	
	/**
	 * 取得上傳日誌資料列表
	 * 
	 * @param qryBo
	 */
	public Page<UploadLogResultListDto> getUploadLogList(QueryUploadLogListBO qryBo);
	
	/**
	 * 取得上傳日誌資料列表(失敗次數 <3 部份)
	 * 
	 * @param qryBo
	 */
	public List<UploadLogResultListDto> getUploadLogFailList();
	
	/**
	 * 取得上傳日誌資料列表(失敗次數 >=3 部份)
	 * 
	 * @param qryBo
	 */
	public List<UploadLogResultListDto> getUploadLogFailThirdList();
	
	/** 取得上傳表單 PDF base64
	 * @param qryBo
	 * @return
	 */
	//public FormPdfDto getUploadFormPdf(UploadLogIdBO idBo);
	public String getUploadFormPdf(UploadLogIdBO idBo);
	
	/** 刪除超過保留期限的上傳日誌
	 * 
	 */
	public void deleteExpiredUploadLog();
	
	/** 重新上傳失敗的表單
	 * @param idBo
	 */
	public void reUploadPdf(UploadLogIdBO idBo);
	
}
