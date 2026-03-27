package com.broton.enote.service;

import java.io.InputStream;
import java.util.List;
import com.broton.enote.bo.ErpCustBO;
import com.broton.enote.bo.ErpCustSaveListBO;
import com.broton.enote.bo.ErpGetStaffsBO;
import com.broton.enote.bo.ErpGetStaffsByPublicBO;
import com.broton.enote.bo.ErpLoginBO;
import com.broton.enote.bo.ErpPhraseBO;
import com.broton.enote.bo.NewCustomerBO;
import com.broton.enote.bo.NewCustomerPdfUploadBO;
import com.broton.enote.bo.PdfDownloadBO;
import com.broton.enote.bo.PdfListBO;
import com.broton.enote.bo.PdfUploadBO;
import com.broton.enote.dto.ErpCustResultListDto;
import com.broton.enote.dto.ErpCustSaveResultListDto;
import com.broton.enote.dto.ErpDoctorResultListDto;
import com.broton.enote.dto.ErpPdfListResultDto;
import com.broton.enote.dto.ErpPhraseResultListDto;
import com.broton.enote.dto.ErpTokenResultDto;

/**
 * 打 ERP Service
 * 
 */
public interface ErpService {
		
	/** 登入
	 * @return
	 */
	public ErpTokenResultDto apiLogin(ErpLoginBO loginBO);
	
	/** 取得(醫師)員工資料列表
	 * @param bo
	 * @return
	 */
	public List<ErpDoctorResultListDto> apiGetStaffs(ErpGetStaffsBO bo);
	
	/** 取得(醫師)員工資料列表by公用帳號
	 * @param bo
	 * @return
	 */
	public List<ErpDoctorResultListDto> apiGetStaffsByPublic(ErpGetStaffsByPublicBO bo);
	
	/** 取得客戶資料列表by公用帳號
	 * @param bo
	 * @return
	 */
	public List<ErpCustResultListDto> apiGetCustByPublic(ErpCustBO bo);
	
	/** 取得客戶資料
	 * @param qryBo
	 * @return
	 */
	public List<ErpCustResultListDto> apiCust(ErpCustBO qryBo);
	
	/** 取得表單分類代碼和名稱
	 * @param qryBo
	 * @return
	 */
	public List<ErpPhraseResultListDto> apiGetPhrase(ErpPhraseBO qryBo);
	
	/** 取得表單分類代碼和名稱by公用帳號
	 * @param qryBo
	 * @return
	 */
	public List<ErpPhraseResultListDto> apiGetPhraseByPublic();

	/** 取得客戶已簽署PDF清單
	 * @param qryBo
	 * @return
	 */
	public List<ErpPdfListResultDto> apiPdfList(PdfListBO qryBo);
	
	/** 上傳新的客戶同意書
	 * @param uploadBo
	 * @return
	 */
	public String apiPdfUpload(PdfUploadBO uploadBo);
	
	/** 以紀錄UNID下載特定的pdf檔案
	 * @param downloadBo
	 * @return base64字串
	 */
	public String apiPdfDownload(PdfDownloadBO downloadBo);
	
	/** 以紀錄UNID下載特定的pdf檔案
	 * @param downloadBo
	 * @return base64字串
	 */
	public InputStream apiPdfDownloadTest(PdfDownloadBO downloadBo);
	
	/** 依客戶編號查詢剩餘課程資料
	 * @param qryBo
	 * @return
	 */
	public List<ErpCustSaveResultListDto> apiCustSaveList(ErpCustSaveListBO qryBo);
	
	/** 平板新增客戶基本資料
	 * @param bo
	 */
	public void newCustomer(NewCustomerBO bo);
	
	/** 上傳新增客戶PDF
	 * @param bo
	 * @return
	 */
	public String newCustomerPdfUpload(NewCustomerPdfUploadBO bo);
}
