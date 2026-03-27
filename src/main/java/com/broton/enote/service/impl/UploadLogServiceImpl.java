package com.broton.enote.service.impl;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.broton.enote.bo.ErpLoginBO;
import com.broton.enote.bo.PdfUploadBO;
import com.broton.enote.bo.QueryUploadLogListBO;
import com.broton.enote.bo.UploadLogIdBO;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.UploadLog;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.UploadLogRepository;
import com.broton.enote.service.ErpService;
import com.broton.enote.service.UploadLogService;
import com.broton.enote.utils.PageUtils;
import com.broton.enote.dto.ErpTokenResultDto;
import com.broton.enote.dto.UploadLogResultListDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UploadLogServiceImpl implements UploadLogService {

	@Autowired
	private UploadLogRepository uploadLogRepository;
	@Autowired
	private ErpService erpService;
	
	@Value("${uploadLog.reserveDay}")	
    private Integer reserveDay; // 上傳日誌保留天數
	@Value("${erp.api.public_uid}")
	private String erpPublicUid;
	@Value("${erp.api.public_pwd}")
	private String erpPublicPwd;
	
	
	@Override
	public Page<UploadLogResultListDto> getUploadLogList(QueryUploadLogListBO qryBo) {
		Page<UploadLogResultListDto> pageDto = null;
		try {
			Pageable pageable = PageUtils.getPageByParameter(qryBo.getStart(), qryBo.getLength(), null);
			pageDto = uploadLogRepository.getUploadLogList(qryBo, pageable);
			log.info("取得上傳日誌列表成功 {}筆", pageDto.getContent().size());
		} catch (Exception e) {
			log.error("取得上傳日誌資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5990.getCode(), ResultCode.ERR_5990.getDesc());
		}
		return pageDto;
	}
	
	@Override
	public List<UploadLogResultListDto> getUploadLogFailList() {
		List<UploadLogResultListDto> listDto = null;
		try {
			listDto = uploadLogRepository.getUploadLogFailList();
			log.info("取得上傳日誌失敗次數 <3 資料列表成功 {}筆", listDto.size());
		} catch (Exception e) {
			log.error("取得上傳日誌失敗次數 <3 資料列表錯誤:{}", e.toString());
			//throw new ErrorCodeException(ResultCode.ERR_5990.getCode(), ResultCode.ERR_5990.getDesc());
		}
		return listDto;
	}
	
	@Override
	public List<UploadLogResultListDto> getUploadLogFailThirdList() {
		List<UploadLogResultListDto> listDto = null;
		try {
			listDto = uploadLogRepository.getUploadLogFailThirdList();
			log.info("取得上傳日誌失敗次數 >=3 資料列表成功 {}筆", listDto.size());
		} catch (Exception e) {
			log.error("取得上傳日誌失敗次數 >=3 資料列表錯誤:{}", e.toString());
			//throw new ErrorCodeException(ResultCode.ERR_5990.getCode(), ResultCode.ERR_5990.getDesc());
		}
		return listDto;
	}
	
	@Override
	public String getUploadFormPdf(UploadLogIdBO idBo) {
		String output = "";
		try {
			UploadLog uploadLog = uploadLogRepository.findById(idBo.getId()).orElse(null);
			if (null != uploadLog) {
				output = Base64.getEncoder().encodeToString(uploadLog.getFileContent());
				log.info("取得上傳日誌表單PDF成功");
			} else {
				log.error("取得上傳日誌表單PDF錯誤,表單資料不存在");
				throw new ErrorCodeException(ResultCode.ERR_5991.getCode(), ResultCode.ERR_5991.getDesc());
			}
		} catch (Exception e) {
			log.error("取得上傳日誌表單PDF錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5992.getCode(), ResultCode.ERR_5992.getDesc());
		}
		return output;
	}
		
	@Override
	public void deleteExpiredUploadLog() {
		if (null != reserveDay) {
			try {
				uploadLogRepository.deleteExpiredUploadLog(reserveDay);
				log.info("刪除超過保留天數{}日的上傳日誌排程執行成功", reserveDay);
			} catch (Exception e) {
				log.error("刪除超過保留天數{}日的上傳日誌排程執行失敗:{}", reserveDay, e.toString());
				throw new ErrorCodeException(ResultCode.ERR_5993.getCode(), ResultCode.ERR_5993.getDesc());
			}
		} else {
			log.error("yml尚未設置上傳日誌的保留天數 uploadLog.reserveDay");
		}
	}
	
	@Override
	public void reUploadPdf(UploadLogIdBO idBo) {
		try {
			UploadLog uploadLog = new UploadLog();
			uploadLog = uploadLogRepository.findById(idBo.getId()).orElse(null); 
			if (null != uploadLog) {
				PdfUploadBO pdfUploadBo = new PdfUploadBO();			
				pdfUploadBo.setPdfBase64(Base64.getEncoder().encodeToString(uploadLog.getFileContent()));
				pdfUploadBo.setPdfName(uploadLog.getFileName());
				pdfUploadBo.setCSID(uploadLog.getCustomerId());
				pdfUploadBo.setDoctorId(uploadLog.getDoctorId());
				pdfUploadBo.setEmployeeId(uploadLog.getCreateUser());
				pdfUploadBo.setFromType(uploadLog.getFileTypeId());
			
				ErpLoginBO loginBO = new ErpLoginBO();
				loginBO.setId(erpPublicUid);
				loginBO.setPass(erpPublicPwd);
				ErpTokenResultDto loginReturn = erpService.apiLogin(loginBO);
				if (null != loginReturn) {
					pdfUploadBo.setToken(loginReturn.getToken());
				}			
				String outVal = "";
				outVal = erpService.apiPdfUpload(pdfUploadBo);
				if (!outVal.equals("Success")) {
					uploadLog.setFailCount(uploadLog.getFailCount() + 1);; // 補上傳失敗,將上傳失敗次數 + 1
					uploadLogRepository.save(uploadLog);
					log.error("重新上傳新的客戶簽署表單失敗:logId={}", idBo.getId());
					throw new ErrorCodeException(ResultCode.ERR_5311.getCode(), ResultCode.ERR_5311.getDesc());
				} else {
					uploadLog.setStatus(1); // 補上傳成功,將狀態改為上傳成功
					uploadLog.setFailCount(0);; // 補上傳成功,將失敗次數改為0
					uploadLogRepository.save(uploadLog);
					log.info("重新上傳新的客戶簽署表單成功:logId={}", idBo.getId());
				}
			}
		} catch (Exception e) {
			log.error("重新上傳新的客戶簽署表單失敗:logId={}, {}", idBo.getId(), e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5311.getCode(), ResultCode.ERR_5311.getDesc());
		}
	}
	
}
