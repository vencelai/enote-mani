package com.broton.enote.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.broton.enote.bo.ErpCustBO;
import com.broton.enote.bo.ErpGetStaffsBO;
import com.broton.enote.bo.FormStageIdBO;
import com.broton.enote.bo.PdfStageBO;
import com.broton.enote.bo.PdfUploadBO;
import com.broton.enote.bo.QueryFormStageListBO;
import com.broton.enote.bo.SetFormStageLockBO;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.FormStage;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.FormStageRepository;
import com.broton.enote.service.ErpService;
import com.broton.enote.service.FormStageService;
import com.broton.enote.utils.PageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.broton.enote.dto.ErpCustResultListDto;
import com.broton.enote.dto.ErpDoctorResultListDto;
import com.broton.enote.dto.FormStageResultListDto;
import com.broton.enote.dto.PdfSignTrackDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FormStageServiceImpl implements FormStageService {

	@Autowired
	private FormStageRepository formStageRepository;
	@Autowired
	private ErpService erpService;
	
	@Value("${uploadLog.reserveDay}")	
    private Integer reserveDay; // 上傳日誌保留天數
	@Value("${erp.api.public_uid}")
	private String erpPublicUid;
	@Value("${erp.api.public_pwd}")
	private String erpPublicPwd;
	
	@Override
	public List<FormStageResultListDto> getFormStageList(QueryFormStageListBO qryBo) {
		List<FormStageResultListDto> pageDto = null;
		try {
			//Pageable pageable = PageUtils.getPageByParameter(qryBo.getStart(), qryBo.getLength(), null);
			pageDto = formStageRepository.getFormStageList(qryBo);
			log.info("取得表單暫存列表成功 {}筆", pageDto.size());
		} catch (Exception e) {
			log.error("取得表單暫存資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5994.getCode(), ResultCode.ERR_5994.getDesc());
		}
		return pageDto;
	}
	
	@Override
	public Page<FormStageResultListDto> getFormStagePageList(QueryFormStageListBO qryBo) {
		Page<FormStageResultListDto> pageDto = null;
		try {
			Pageable pageable = PageUtils.getPageByParameter(qryBo.getStart(), qryBo.getLength(), null);
			pageDto = formStageRepository.getFormStagePageList(qryBo, pageable);
			log.info("取得表單暫存列表成功 {}筆", pageDto.getContent().size());
		} catch (Exception e) {
			log.error("取得表單暫存資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5994.getCode(), ResultCode.ERR_5994.getDesc());
		}
		return pageDto;
	}
	
	@Override
	public PdfSignTrackDto getFormStagePdfSignTrack(FormStageIdBO idBo) {
		PdfSignTrackDto output = null;
		try {
			FormStage formStage = formStageRepository.findById(idBo.getId()).orElse(null);
			if (null != formStage) {
				output = new PdfSignTrackDto();
				if (null != formStage.getFileContent()) {
					output.setPdfBase64(Base64.getEncoder().encodeToString(formStage.getFileContent()));
				}
				if (null != formStage.getSignTrack()) {
					output.setSignTrackBase64(Base64.getEncoder().encodeToString(formStage.getSignTrack()));
				}
				ErpCustBO custQryBo = new ErpCustBO();
				custQryBo.setCustFindStr(formStage.getCustomerId());
				custQryBo.setToken(idBo.getToken());
				List<ErpCustResultListDto> custList = erpService.apiCust(custQryBo);
				if (custList.size() > 0) {
					// listDto 需作轉換的動作,不然直接取會報錯
					ObjectMapper mapper = new ObjectMapper();
					List<ErpCustResultListDto> dto = new ArrayList<ErpCustResultListDto>();		
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(custList);				
					dto = Arrays.asList(mapper.readValue(json, ErpCustResultListDto[].class));
					output.setCustomerId(dto.get(0).getCsid());
					output.setCustomerName(dto.get(0).getCustCname());
					output.setCustomerPhone(dto.get(0).getGsmtel());
					output.setBirthday(dto.get(0).getBirthday());
				}
				log.info("取得表單暫存PDF,簽名軌跡成功");
			} else {
				log.error("表單暫存資料不存在");
				throw new ErrorCodeException(ResultCode.ERR_5996.getCode(), ResultCode.ERR_5996.getDesc());
			}
		} catch (Exception e) {
			log.error("取得表單暫存PDF,簽名軌跡錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5997.getCode(), ResultCode.ERR_5997.getDesc());
		}
		return output;
	}
	
	@Override
	public void terminationFormStage(FormStageIdBO idBo) {
		try {
			if (formStageRepository.existsById(idBo.getId())) {
				FormStage formStage = formStageRepository.findById(idBo.getId()).orElse(null);
				if (null != formStage) {
					formStage.setTermination("y"); // 標註為棄用
					formStageRepository.save(formStage);
					log.info("表單暫存棄用成功");
				}
			} else {
				log.error("表單暫存資料不存在");
				throw new ErrorCodeException(ResultCode.ERR_5996.getCode(), ResultCode.ERR_5996.getDesc());
			}
		} catch (Exception e) {
			log.error("表單暫存棄用錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5995.getCode(), ResultCode.ERR_5995.getDesc());
		}		
	}
	
	@Override
	public void formStageImport(PdfStageBO bo) {
		try {
			//log.info(bo);
			// 取出客戶姓名,手機
			String custName = "";
			String custPhone = "";
			ErpCustBO custQryBo = new ErpCustBO();
			custQryBo.setCustFindStr(bo.getCSID());
			custQryBo.setToken(bo.getToken());
			List<ErpCustResultListDto> custList = erpService.apiCust(custQryBo);
			if (custList.size() > 0) {
				// listDto 需作轉換的動作,不然直接取會報錯
				ObjectMapper mapper = new ObjectMapper();
				List<ErpCustResultListDto> dto = new ArrayList<ErpCustResultListDto>();		
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(custList);				
				dto = Arrays.asList(mapper.readValue(json, ErpCustResultListDto[].class));
				custName = dto.get(0).getCustCname();
				custPhone = dto.get(0).getGsmtel();
			}
			// 取出醫師姓名
			String doctorName = null;			
			if (bo.getDoctorId() != null) {
				ErpGetStaffsBO doctorBo = new ErpGetStaffsBO();
				doctorBo.setStfNo(bo.getDoctorId());
				doctorBo.setToken(bo.getToken());
				List<ErpDoctorResultListDto> doctorList = erpService.apiGetStaffs(doctorBo);
				if (doctorList.size() > 0) {
					// listDto 需作轉換的動作,不然直接取會報錯
					ObjectMapper mapper = new ObjectMapper();
					List<ErpDoctorResultListDto> dto = new ArrayList<ErpDoctorResultListDto>();		
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(doctorList);				
					dto = Arrays.asList(mapper.readValue(json, ErpDoctorResultListDto[].class));
					doctorName = dto.get(0).getStfName();
				}
			}
			
			if (null == bo.getId()) {
				FormStage formStage = new FormStage();
				formStage.setBranchId(bo.getBranchId());
				formStage.setCustomerId(bo.getCSID());
				formStage.setCustomerName(custName);
				formStage.setCustomerPhone(custPhone);
				formStage.setFileName(bo.getPdfName());
				
				FileNameAwareByteArrayResource fileArray = null;
				// 解析 base64 pdf 字串 
				if (null != bo.getPdfBase64()) {
					byte[] decodedBytes;
					decodedBytes = Base64.getDecoder().decode(bo.getPdfBase64());
					fileArray = new FileNameAwareByteArrayResource(bo.getPdfName(), decodedBytes, null);
					formStage.setFileContent(fileArray.getByteArray());
				}
				
				// 解析 base64 簽名軌跡 字串
				if (null != bo.getSignTrackBase64()) {
					byte[] decodedBytes2;
					decodedBytes2 = Base64.getDecoder().decode(bo.getSignTrackBase64());
					formStage.setSignTrack(decodedBytes2);
				}
				
				formStage.setFormId(bo.getFormId());
				formStage.setFileTypeId(bo.getFromType());
				formStage.setDoctorId(bo.getDoctorId());
				formStage.setDoctorName(doctorName);
				//formStage.setStatus(isSuccess ? 1 : 0);
				//formStage.setFailCount(isSuccess ? 0 : 1);
				formStage.setTermination("n"); // 標註為不棄用
				formStage.setCreateUser(bo.getEmployeeId());
				formStage.setCreateDate(new Date());
				formStageRepository.save(formStage);
				log.info("新增表單暫存成功");
			} else {
				FormStage formStage = formStageRepository.findById(bo.getId()).orElse(null);
				if (null != formStage) {
					FileNameAwareByteArrayResource fileArray = null;
					// 解析 base64 pdf 字串 
					if (null != bo.getPdfBase64()) {
						byte[] decodedBytes;
						decodedBytes = Base64.getDecoder().decode(bo.getPdfBase64());
						fileArray = new FileNameAwareByteArrayResource(bo.getPdfName(), decodedBytes, null);
						formStage.setFileContent(fileArray.getByteArray());
					}
					
					// 解析 base64 簽名軌跡 字串
					if (null != bo.getSignTrackBase64()) {
						byte[] decodedBytes2;
						decodedBytes2 = Base64.getDecoder().decode(bo.getSignTrackBase64());
						formStage.setSignTrack(decodedBytes2);
					}
					
					formStage.setBranchId(bo.getBranchId());
					// 回復為未鎖定狀態
					formStage.setLockAd(null);
					formStage.setLockAdName(null);
					formStage.setLockDate(null);
					formStage.setCreateUser(bo.getEmployeeId());
					formStage.setCreateDate(new Date());
					formStageRepository.save(formStage);
					log.info("表單暫存修改成功");
				}
			}
		} catch (Exception e) {
			log.error("表單暫存錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5998.getCode(), ResultCode.ERR_5998.getDesc());
		}
		
	}
	
	@Override
	public FormStageResultListDto getFormStageById(FormStageIdBO idBo) {
		FormStageResultListDto output = null;
		try {
			if (formStageRepository.existsById(idBo.getId())) {
				output = formStageRepository.getFormStageById(idBo);
				if (null == output.getLockDate()) {
					log.info("取得表單暫存資料成功 id:{}", idBo.getId());
				} else {
					if (!output.getLockAd().equals(idBo.getEmployeeId())) {
						log.error("取得表單暫存錯誤,表單已被鎖定 {}", output.getId());
						throw new ErrorCodeException(ResultCode.ERR_59991.getCode(), ResultCode.ERR_59991.getDesc());
					}
				}
			} else {
				log.error("表單暫存資料不存在 id:{}", idBo.getId());
				throw new ErrorCodeException(ResultCode.ERR_5996.getCode(), ResultCode.ERR_5996.getDesc());
			}
		} catch (Exception e) {
			log.error("取得表單暫存錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5999.getCode(), ResultCode.ERR_5999.getDesc());
		}
		return output;
	}
	
	/** 檔案不落地處理
	 */
	public class FileNameAwareByteArrayResource extends ByteArrayResource {
        private String fileName;
        public FileNameAwareByteArrayResource(String fileName, byte[] byteArray, String description) {
            super(byteArray, description);
            this.fileName = fileName;
        }
        @Override
        public String getFilename() {
            return fileName;
        }
	}

	@Override
	public void formStageUploadToErp(PdfStageBO bo) {
		if (formStageRepository.existsById(bo.getId())) {
			FormStage formStage = formStageRepository.findById(bo.getId()).orElse(null);
			if (null != formStage) {
				if (formStage.getTermination().equals("n")) {
					PdfUploadBO pdfUploadBO = new PdfUploadBO();
					pdfUploadBO.setBranchId(bo.getBranchId());
					pdfUploadBO.setPdfBase64(bo.getPdfBase64());
					pdfUploadBO.setPdfName(bo.getPdfName());
					pdfUploadBO.setCSID(bo.getCSID());
					pdfUploadBO.setDoctorId(bo.getDoctorId());
					pdfUploadBO.setEmployeeId(bo.getEmployeeId());
					pdfUploadBO.setFromType(bo.getFromType());
					pdfUploadBO.setToken(bo.getToken());
					erpService.apiPdfUpload(pdfUploadBO);			
					// 刪除表單暫存資料		
					formStageRepository.deleteById(bo.getId());		
					log.info("刪除暫存表單 {} 成功", bo.getId());
				} else {
					log.error("此份表單暫存資料已被棄用 {}", bo.getId());
					throw new ErrorCodeException(ResultCode.ERR_6002.getCode(), ResultCode.ERR_6002.getDesc());
				}
			}
		} else {
			log.error("此份表單暫存資料已被上傳 {}", bo.getId());
			throw new ErrorCodeException(ResultCode.ERR_6001.getCode(), ResultCode.ERR_6001.getDesc());
		}
	}
	
	@Override
	public void abortFormStageLock(FormStageIdBO idBo) {
		try {
			if (formStageRepository.existsById(idBo.getId())) {
				FormStage formStage = formStageRepository.findById(idBo.getId()).orElse(null);
				if (null != formStage) {
					formStage.setLockAd(null);
					formStage.setLockAdName(null);
					formStage.setLockDate(null);
					formStageRepository.save(formStage);
					log.info("解除表單暫存鎖定成功");
				}
			} else {
				log.error("表單暫存資料不存在");
				throw new ErrorCodeException(ResultCode.ERR_5996.getCode(), ResultCode.ERR_5996.getDesc());
			}
		} catch (Exception e) {
			log.error("解除表單暫存鎖定錯誤: id:{} {}", idBo.getId(), e.toString());
			throw new ErrorCodeException(ResultCode.ERR_59951.getCode(), ResultCode.ERR_59951.getDesc());
		}		
	}

	@Override
	public void setFormStageLock(SetFormStageLockBO bo) {
		try {
			if (formStageRepository.existsById(bo.getId())) {
				FormStage formStage = formStageRepository.findById(bo.getId()).orElse(null);
				if (null != formStage) {
					if (null == formStage.getLockDate()) {
						formStage.setLockAd(bo.getLockAd());
						formStage.setLockAdName(bo.getLockAdName());
						formStage.setLockDate(new Date());
						formStageRepository.save(formStage);
						log.info("設定表單暫存鎖定成功");
					}
				}
			} else {
				log.error("表單暫存資料不存在");
				throw new ErrorCodeException(ResultCode.ERR_5996.getCode(), ResultCode.ERR_5996.getDesc());
			}
		} catch (Exception e) {
			log.error("設定表單暫存鎖定錯誤: id:{} {}", bo.getId(), e.toString());
			throw new ErrorCodeException(ResultCode.ERR_59952.getCode(), ResultCode.ERR_59952.getDesc());
		}
		
	}

	@Override
	public void deleteExpiredFormStage() {
		try {
			formStageRepository.deleteExpiredFormStage(reserveDay);
			log.info("刪除超過保留期限的表單暫存資料(只針對棄用的)成功");
		} catch (Exception e) {
			log.error("刪除超過保留期限的表單暫存資料(只針對棄用的)錯誤 {}", e.toString());
		}
		
	}
}
