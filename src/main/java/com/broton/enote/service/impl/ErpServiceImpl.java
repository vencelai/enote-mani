package com.broton.enote.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.UploadLog;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.DeviceRepository;
import com.broton.enote.repository.UploadLogRepository;
import com.broton.enote.service.ErpService;
import com.broton.enote.utils.ApiUtils;
import com.broton.enote.utils.CommonUtil;
import com.broton.enote.utils.ExternalIP;
import com.broton.enote.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import com.broton.enote.dto.RetConsult;
import com.broton.enote.repository.BranchRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ErpServiceImpl implements ErpService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private UploadLogRepository uploadLogRepository;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private BranchRepository branchRepository;

	@Value("${erp.api.url}")
	private String erpApiUrl;
	@Value("${erp.api.vid}")
	private String erpApiVid;
	@Value("${erp.api.vip}")
	private String erpApiVip;
	@Value("${erp.api.public_uid}")
	private String erpPublicUid;
	@Value("${erp.api.public_pwd}")
	private String erpPublicPwd;
	@Value("${customer.newCustomerFormId}")
	private BigInteger newCustomerFormId;
	
	@Override
	public ErpTokenResultDto apiLogin(ErpLoginBO loginBO) {
		// 先判斷設備ID是否存在
		if (null != loginBO.getDeviceId()) {
			BigInteger deviceNo = deviceRepository.getDeviceNo(loginBO.getDeviceId());
			if (null == deviceNo) {
				log.error("不存在的設備ID:{}", loginBO.getDeviceId());
				throw new ErrorCodeException(ResultCode.ERR_5953.getCode(), ResultCode.ERR_5953.getDesc());
			}
		}
		// 由設備 id 取出對映的據點 id
		BigInteger branchId = deviceRepository.getDeviceBranch(loginBO.getDeviceId());
		
		ErpTokenResultDto output = new ErpTokenResultDto();
		try {
			String url = erpApiUrl + "/api/Login";			
			url += generateBaseVerify();

			Map<String, Object> params = new HashMap<>();
			params = CommonUtil.objectToMap(loginBO);
			
			ErpTokenResultDto tokenResultDto = ApiUtils.postDate(restTemplate, url, params, null, ErpTokenResultDto.class);
			if (null != tokenResultDto) {
				log.info("員工登入成功 userId:{}", loginBO.getId());
				tokenResultDto.setUserId(loginBO.getId());
				output = tokenResultDto;
				output.setBranchId(branchId);
				output.setCurrentDateTime(new Date().getTime());
				output.setNewCustomerFormId(newCustomerFormId);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("員工登入失敗,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("400 Bad Request");
			if (containsStr) {
				log.error("員工登入失敗,帳號或密碼錯誤");
				throw new ErrorCodeException(ResultCode.ERR_5302.getCode(), ResultCode.ERR_5302.getDesc());
			} else {
				log.error("員工登入失敗 userId:{} {}", loginBO.getId(), e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5304.getCode(), ResultCode.ERR_5304.getDesc());
			}
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ErpDoctorResultListDto> apiGetStaffs(ErpGetStaffsBO bo) {
		List<ErpDoctorResultListDto> output = new ArrayList<ErpDoctorResultListDto>();
		try {
			String url = erpApiUrl + "/api/GetStaffs";			
			url += generateBaseVerify();

			Map<String, Object> params = new HashMap<>();
			params = CommonUtil.objectToMap(bo);
			params.remove("token");
			
			output = ApiUtils.postDate(restTemplate, url, params, bo.getToken(), (new ArrayList<ErpDoctorResultListDto>()).getClass());
			log.info("取得(醫師)員工資料列表成功 {} 筆", output.size());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("取得(醫師)員工資料列表失敗,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得(醫師)員工資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得(醫師)員工資料列表錯誤 {}", e.toString());
				throw new ErrorCodeException(ResultCode.ERR_5971.getCode(), ResultCode.ERR_5971.getDesc());
			}
		}
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErpDoctorResultListDto> apiGetStaffsByPublic(ErpGetStaffsByPublicBO bo) {
		List<ErpDoctorResultListDto> output = new ArrayList<ErpDoctorResultListDto>();
		try {
			String url = erpApiUrl + "/api/GetStaffs";			
			url += generateBaseVerify();
			
			ErpLoginBO loginBO = new ErpLoginBO();
			loginBO.setId(erpPublicUid);
			loginBO.setPass(erpPublicPwd);
			ErpTokenResultDto loginReturn = apiLogin(loginBO);
			if (null != loginReturn) {
				Map<String, Object> params = new HashMap<>();
				params = CommonUtil.objectToMap(bo);
			
				output = ApiUtils.postDate(restTemplate, url, params, loginReturn.getToken(), (new ArrayList<ErpDoctorResultListDto>()).getClass());
				log.info("取得(醫師)員工資料列表成功 {} 筆", output.size());
			} else {
				log.error("取得(醫師)員工資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("取得(醫師)員工資料列表失敗,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得(醫師)員工資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得(醫師)員工資料列表錯誤 {}", e.toString());
				throw new ErrorCodeException(ResultCode.ERR_5971.getCode(), ResultCode.ERR_5971.getDesc());
			}
		}
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErpCustResultListDto> apiCust(ErpCustBO qryBo) {
		ExternalIP.init();
		List<ErpCustResultListDto> output = new ArrayList<ErpCustResultListDto>();
		try {
			if (null == qryBo.getCustFindStr() || "".equals(qryBo.getCustFindStr())) {
				log.error("取得客戶資料列表錯誤,無輸入查詢條件(手機,姓名,客代關鍵字)");
				throw new ErrorCodeException(ResultCode.ERR_5002.getCode(), ResultCode.ERR_5002.getDesc());
			}			
			String url = erpApiUrl + "/api/Cust";			
			url += generateBaseVerify();

			Map<String, Object> params = new HashMap<>();
			params = CommonUtil.objectToMap(qryBo);
			params.remove("token");
			
			output = ApiUtils.postDate(restTemplate, url, params, qryBo.getToken(), (new ArrayList<ErpCustResultListDto>()).getClass());
			log.info("取得客戶資料列表成功 {} 筆", output.size());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("取得客戶資料列表失敗,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得客戶資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得客戶資料列表錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5001.getCode(), ResultCode.ERR_5001.getDesc());
			}
		}
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErpPhraseResultListDto> apiGetPhrase(ErpPhraseBO qryBo) {
		List<ErpPhraseResultListDto> output = new ArrayList<ErpPhraseResultListDto>();
		try {
			if (null == qryBo.getPhraseType() || "".equals(qryBo.getPhraseType())) {
				log.error("取得同意書分類資料列表錯誤,無輸入查詢條件 PhraseType");
				throw new ErrorCodeException(ResultCode.ERR_5305.getCode(), ResultCode.ERR_5305.getDesc());
			}			
			String url = erpApiUrl + "/api/GetPhrase";			
			url += generateBaseVerify();

			Map<String, Object> params = new HashMap<>();
			if (qryBo.getPhraseType() == null) {
				qryBo.setPhraseType("PdfFileKind");
			}
			params = CommonUtil.objectToMap(qryBo);
			params.remove("token");
			
			output = ApiUtils.postDate(restTemplate, url, params, qryBo.getToken(), (new ArrayList<ErpPhraseResultListDto>()).getClass());
			log.info("取得同意書分類資料列表成功 {} 筆", output.size());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("取得同意書分類資料列表錯誤,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得同意書分類資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得同意書分類資料列表錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5306.getCode(), ResultCode.ERR_5306.getDesc());
			}
		}
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErpPhraseResultListDto> apiGetPhraseByPublic() {
		List<ErpPhraseResultListDto> output = new ArrayList<ErpPhraseResultListDto>();
		try {					
			String url = erpApiUrl + "/api/GetPhrase";			
			url += generateBaseVerify();
			
			ErpLoginBO loginBO = new ErpLoginBO();
			loginBO.setId(erpPublicUid);
			loginBO.setPass(erpPublicPwd);
			ErpTokenResultDto loginReturn = apiLogin(loginBO);
			if (null != loginReturn) {
				ErpPhraseBO qryBo = new ErpPhraseBO();
				qryBo.setToken(loginReturn.getToken());
				qryBo.setPhraseType("PdfFileKind");

				Map<String, Object> params = new HashMap<>();
				params = CommonUtil.objectToMap(qryBo);
				params.remove("token");
			
				output = ApiUtils.postDate(restTemplate, url, params, qryBo.getToken(), (new ArrayList<ErpPhraseResultListDto>()).getClass());
				log.info("取得同意書分類資料列表成功 {} 筆", output.size());
			} else {
				log.error("取得同意書分類資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("取得同意書分類資料列表錯誤,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得同意書分類資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得同意書分類資料列表錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5306.getCode(), ResultCode.ERR_5306.getDesc());
			}
		}
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErpPdfListResultDto> apiPdfList(PdfListBO qryBo) {
		List<ErpPdfListResultDto> output = new ArrayList<ErpPdfListResultDto>();
		try {					
			String url = erpApiUrl + "/api/PDF/List/" + qryBo.getCsid();			
			url += generateBaseVerify();
			
			output = ApiUtils.getData(restTemplate, url, null, qryBo.getToken(), (new ArrayList<ErpPdfListResultDto>()).getClass());
			log.info("取得客戶已簽署PDF清單成功 {} 筆", output.size());
		} catch (IllegalArgumentException e) {
			log.error("取得客戶已簽署PDF清單錯誤,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得客戶已簽署PDF清單錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得客戶已簽署PDF清單錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5307.getCode(), ResultCode.ERR_5307.getDesc());
			}
		}
		return output;
	}
	
	@Override
	public String apiPdfUpload(PdfUploadBO uploadBo) {
		String outVal = "Fail";
		FileNameAwareByteArrayResource fileArray = null;
		try {
			// 解析 base64 pdf 字串 
			byte[] decodedBytes;
			decodedBytes = Base64.getDecoder().decode(uploadBo.getPdfBase64());
			fileArray = new FileNameAwareByteArrayResource(uploadBo.getPdfName(), decodedBytes, null);
			
			// 組裝表頭參數
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			if (StringUtils.isNotBlank(uploadBo.getToken())) {
				log.debug("have token = {}", uploadBo.getToken());
				headers.setBearerAuth(uploadBo.getToken());
			}			
			// 組裝body參數
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileArray);
			body.add("pdfname", uploadBo.getPdfName());
			body.add("CSID", uploadBo.getCSID());
			body.add("FromType", uploadBo.getFromType());
			body.add("StfnoDrSrv", uploadBo.getDoctorId());
			
			String url = erpApiUrl + "/api/PDF/Upload";			
			url += generateBaseVerify();
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
			ResponseEntity<Object> response = restTemplate.postForEntity(url, requestEntity, Object.class);
			String output = response.getBody().toString();
			if ("Success".equals(output)) {
				processUploadLog(true, uploadBo, fileArray);
				log.info("上傳新的客戶簽署表單成功: {}", output);							
				outVal = "Success";
			} else if ("Upload Error".equals(output)) {
				processUploadLog(false, uploadBo, fileArray);
				log.error("上傳新的客戶簽署表單失敗1: {}", output);
				throw new ErrorCodeException(ResultCode.ERR_5308.getCode(), ResultCode.ERR_5308.getDesc());				
			}
		} catch (Exception e) {
			processUploadLog(false, uploadBo, fileArray);
			log.error("上傳新的客戶簽署表單失敗2: {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5308.getCode(), ResultCode.ERR_5308.getDesc());
		}
		return outVal;
	}
	
	/** 寫入上傳日誌
	 * @param isSuccess
	 * @param uploadBo
	 * @param pdfPath
	 */
	public void processUploadLog(boolean isSuccess, PdfUploadBO uploadBo, FileNameAwareByteArrayResource fileByteArray) {
		try {
			// 取出客戶姓名,手機
			String custName = "";
			String custPhone = "";
			ErpCustBO custQryBo = new ErpCustBO();
			custQryBo.setCustFindStr(uploadBo.getCSID());
			custQryBo.setToken(uploadBo.getToken());
			List<ErpCustResultListDto> custList = apiCust(custQryBo);
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
			if (uploadBo.getDoctorId() != null) {
				ErpGetStaffsBO doctorBo = new ErpGetStaffsBO();
				doctorBo.setStfNo(uploadBo.getDoctorId());
				doctorBo.setToken(uploadBo.getToken());
				List<ErpDoctorResultListDto> doctorList = apiGetStaffs(doctorBo);
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
			
			UploadLog uploadLog = new UploadLog();
			uploadLog.setBranchId(uploadBo.getBranchId());
			uploadLog.setCustomerId(uploadBo.getCSID());
			uploadLog.setCustomerName(custName);
			uploadLog.setCustomerPhone(custPhone);
			uploadLog.setFileName(uploadBo.getPdfName());		
			uploadLog.setFileContent(fileByteArray.getByteArray());
			uploadLog.setFileTypeId(uploadBo.getFromType());
			uploadLog.setDoctorId(uploadBo.getDoctorId());
			uploadLog.setDoctorName(doctorName);
			uploadLog.setStatus(isSuccess ? 1 : 0);
			uploadLog.setFailCount(isSuccess ? 0 : 1);
			uploadLog.setCreateUser(uploadBo.getEmployeeId());
			uploadLog.setCreateDate(new Date());
			uploadLogRepository.save(uploadLog);
			log.info("寫入上傳日誌成功");
		} catch (Exception e) {
			log.error("寫入上傳日誌錯誤:{}", e.toString());
		}
	}
	
	@Override
	public String apiPdfDownload(PdfDownloadBO downloadBo) {
		String base64Pdf = "";
		try {
			String url = erpApiUrl + "/api/PDF/Download/" + downloadBo.getUnid();			
			url += generateBaseVerify();			
			// 組裝表頭參數
			HttpHeaders headers = new HttpHeaders();
			if (StringUtils.isNotBlank(downloadBo.getToken())) {
				log.debug("have token = {}", downloadBo.getToken());
				headers.setBearerAuth(downloadBo.getToken());
			}
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			ResponseEntity<Resource> in = restTemplate.exchange(url, HttpMethod.GET, entity, Resource.class);
			// 將取回的檔案(不落地)轉成base64 
			try {
				InputStream is = in.getBody().getInputStream();
				byte[] pdfBytes = new byte[is.available()];
				is.read(pdfBytes, 0, pdfBytes.length);
				is.close();
				base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
				log.info("以紀錄UNID下載特定的pdf檔案成功:{} bytes", base64Pdf.length());
			} catch (IOException e) {
				log.error("以紀錄UNID下載特定的pdf檔案錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5309.getCode(), ResultCode.ERR_5309.getDesc());
			}			
		} catch (IllegalArgumentException e) {
			log.error("以紀錄UNID下載特定的pdf檔案錯誤,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("以紀錄UNID下載特定的pdf檔案錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("以紀錄UNID下載特定的pdf檔案錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5309.getCode(), ResultCode.ERR_5309.getDesc());
			}
		}
		return base64Pdf;
	}
	
	@Override
	public InputStream apiPdfDownloadTest(PdfDownloadBO downloadBo) {
		InputStream is = null;
		try {
			String url = erpApiUrl + "/api/PDF/Download/" + downloadBo.getUnid();			
			url += generateBaseVerify();			
			// 組裝表頭參數
			HttpHeaders headers = new HttpHeaders();
			if (StringUtils.isNotBlank(downloadBo.getToken())) {
				log.debug("have token = {}", downloadBo.getToken());
				headers.setBearerAuth(downloadBo.getToken());
			}
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			ResponseEntity<Resource> in = restTemplate.exchange(url, HttpMethod.GET, entity, Resource.class);
			try {
				is = in.getBody().getInputStream();
			} catch (IOException e) {
				log.error("以紀錄UNID下載特定的pdf檔案錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5309.getCode(), ResultCode.ERR_5309.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("以紀錄UNID下載特定的pdf檔案錯誤,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("以紀錄UNID下載特定的pdf檔案錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("以紀錄UNID下載特定的pdf檔案錯誤 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5309.getCode(), ResultCode.ERR_5309.getDesc());
			}
		}
		return is;
	}
	
	/** 產出ERP-API基礎驗證資料
	 * @return
	 */
	public String generateBaseVerify() {		
		String output = "";
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String erpApiVtime = sdFormat.format(new Date());
		output = "?vid=" + erpApiVid + "&vip=" + erpApiVip + "&vtime=" + erpApiVtime;
		//output = "?vid=" + erpApiVid + "&vip=" + ExternalIP.ipAddress + "&vtime=" + erpApiVtime;
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
	
	@SuppressWarnings("unchecked")
	public List<ErpCustSaveResultListDto> apiCustSaveList(ErpCustSaveListBO qryBo) {
		List<ErpCustSaveResultListDto> output = new ArrayList<ErpCustSaveResultListDto>();
		try {					
			String url = erpApiUrl + "/api/Cust/SaveList/" + qryBo.getCsid();			
			url += generateBaseVerify();
			
			output = ApiUtils.getData(restTemplate, url, null, qryBo.getToken(), (new ArrayList<ErpCustSaveResultListDto>()).getClass());
			log.info("依客戶編號查詢剩餘課程資料成功 {} 筆", output.size());
		} catch (IllegalArgumentException e) {
			log.error("依客戶編號查詢剩餘課程資料錯誤,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("依客戶編號查詢剩餘課程資料錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("依客戶編號查詢剩餘課程資料失敗 {}", e.getMessage());
				throw new ErrorCodeException(ResultCode.ERR_5310.getCode(), ResultCode.ERR_5310.getDesc());
			}
		}
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErpCustResultListDto> apiGetCustByPublic(ErpCustBO bo) {
		List<ErpCustResultListDto> output = new ArrayList<ErpCustResultListDto>();
		try {
			String url = erpApiUrl + "/api/Cust";			
			url += generateBaseVerify();
			
			ErpLoginBO loginBO = new ErpLoginBO();
			loginBO.setId(erpPublicUid);
			loginBO.setPass(erpPublicPwd);
			ErpTokenResultDto loginReturn = apiLogin(loginBO);
			if (null != loginReturn) {
				Map<String, Object> params = new HashMap<>();
				params = CommonUtil.objectToMap(bo);
				params.remove("token");
				output = ApiUtils.postDate(restTemplate, url, params, loginReturn.getToken(), (new ArrayList<ErpCustResultListDto>()).getClass());
				log.info("取得客戶資料列表成功 {} 筆", output.size());
			} else {
				log.error("取得客戶資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("取得客戶資料列表失敗,物件轉換失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_4000.getCode(), ResultCode.ERR_4000.getDesc());
		} catch (RestClientException e) {
			boolean containsStr = e.getMessage().contains("401 Unauthorized");
			if (containsStr) {
				log.error("取得客戶資料列表錯誤,不是合法的使用者");
				throw new ErrorCodeException(ResultCode.ERR_3101.getCode(), ResultCode.ERR_3101.getDesc());
			} else {
				log.error("取得客戶資料列表錯誤 {}", e.toString());
				throw new ErrorCodeException(ResultCode.ERR_5975.getCode(), ResultCode.ERR_5975.getDesc());
			}
		}
		return output;
	}
	
	@Override
	public void newCustomer(NewCustomerBO bo) {
		try {
			// 打 ERP 的新增客戶 api
			String url = erpApiUrl + "/api/Consult/NewCustConsult";			
			url += generateBaseVerify();
			
			String erpBranchNo = branchRepository.getBranchErpId(bo.getBranchId());
			bo.setBrNo(erpBranchNo);
			bo.setCsid(null);
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");			
			bo.setConsultDate(sdFormat.format(new Date()));
			//log.info(JsonUtils.objectToJson(bo));
			
			Map<String, Object> params = new HashMap<>();
			params = CommonUtil.objectToMap(bo);
			// 移除上傳pdf相關欄位
			params.remove("branchId");
			params.remove("pdfBase64");
			params.remove("pdfName");
			params.remove("token");
			RetConsult retConsult = ApiUtils.postDate(restTemplate, url, params, bo.getToken(), RetConsult.class);			
			
			if (null != retConsult && (retConsult.getRetCode().equals("1") || retConsult.getRetCode().equals("0"))) {
				log.info("平板新增客戶基本資料成功 {}", retConsult);
				if (StringUtils.isNotBlank(retConsult.getCsid())) {
					// 打 ERP 上傳新增客戶的 pdf 檔案 api
					NewCustomerPdfUploadBO pdfParam = new NewCustomerPdfUploadBO();
					pdfParam.setBranchId(bo.getBranchId());
					pdfParam.setCSID(retConsult.getCsid());
					pdfParam.setPdfBase64(bo.getPdfBase64());
					pdfParam.setFromType("K8");
					pdfParam.setPdfName("初診諮詢卡.pdf");
					pdfParam.setToken(bo.getToken());
					pdfParam.setEmployeeId(bo.getUserID());
					newCustomerPdfUpload(pdfParam);
				}
			} else {
				log.error("平板新增客戶錯誤,ERP返回存檔錯誤 {}", retConsult.getRetMessage());
				throw new ErrorCodeException(ResultCode.ERR_5718.getCode(), retConsult.getRetMessage());
			}
		} catch (Exception e) {
			log.error("平板新增客戶錯誤 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5718.getCode(), ResultCode.ERR_5718.getDesc());
		}
	}
	
	@Override
	public String newCustomerPdfUpload(NewCustomerPdfUploadBO uploadBo) {
		String outVal = "Fail";
		FileNameAwareByteArrayResource fileArray = null;
		try {
			// 解析 base64 pdf 字串 
			byte[] decodedBytes;
			decodedBytes = Base64.getDecoder().decode(uploadBo.getPdfBase64());
			
			//------ 上傳的pdf填入ERP新增客戶api返回的病歷號碼
			try {
				PDDocument pdfDoc = PDDocument.load(decodedBytes);
				PDPage pdfPage = pdfDoc.getPage(0);
				PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdfPage, PDPageContentStream.AppendMode.APPEND, true, true);
				contentStream.beginText();
				contentStream.newLineAtOffset(98, 765);
				PDFont pdfFont= PDType1Font.HELVETICA_BOLD;
				int fontSize = 14;
				contentStream.setFont(pdfFont, fontSize);
				contentStream.showText(uploadBo.getCSID());
				contentStream.endText();
				contentStream.close();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				pdfDoc.save(byteArrayOutputStream);
				fileArray = new FileNameAwareByteArrayResource(uploadBo.getPdfName(), byteArrayOutputStream.toByteArray(), null);
				pdfDoc.close();
				log.info("PDF 填入病歷號碼成功 {}", uploadBo.getCSID());
			} catch (Exception e) {
				log.error("PDF 填入病歷號碼錯誤 {} {}", uploadBo.getCSID(), e.toString());
				throw new ErrorCodeException(ResultCode.ERR_5313.getCode(), ResultCode.ERR_5313.getDesc());
			}
			
			//fileArray = new FileNameAwareByteArrayResource(uploadBo.getPdfName(), decodedBytes, null);			
			
			// 組裝表頭參數
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			if (StringUtils.isNotBlank(uploadBo.getToken())) {
				log.debug("have token = {}", uploadBo.getToken());
				headers.setBearerAuth(uploadBo.getToken());
			}			
			// 組裝body參數
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileArray);
			body.add("pdfname", uploadBo.getPdfName());
			body.add("CSID", uploadBo.getCSID());
			body.add("FromType", uploadBo.getFromType());
			//body.add("StfnoDrSrv", uploadBo.getDoctorId());
			
			String url = erpApiUrl + "/api/PDF/Upload";			
			url += generateBaseVerify();
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
			ResponseEntity<Object> response = restTemplate.postForEntity(url, requestEntity, Object.class);
			String output = response.getBody().toString();
			
			PdfUploadBO pdfUploadBO = new PdfUploadBO();
			pdfUploadBO.setBranchId(uploadBo.getBranchId());
			pdfUploadBO.setPdfBase64(uploadBo.getPdfBase64());
			pdfUploadBO.setPdfName(uploadBo.getPdfName());
			pdfUploadBO.setCSID(uploadBo.getCSID());
			pdfUploadBO.setEmployeeId(uploadBo.getEmployeeId());
			pdfUploadBO.setFromType(uploadBo.getFromType());
			pdfUploadBO.setToken(uploadBo.getToken());
			
			if ("Success".equals(output)) {				
				processUploadLog(true, pdfUploadBO, fileArray);
				log.info("平板上傳新增客戶PDF成功: {}", output);							
				outVal = "Success";
			} else if ("Upload Error".equals(output)) {
				processUploadLog(false, pdfUploadBO, fileArray);
				log.error("平板上傳新增客戶PDF失敗: {}", output);
				throw new ErrorCodeException(ResultCode.ERR_5312.getCode(), ResultCode.ERR_5312.getDesc());				
			}
		} catch (Exception e) {
			log.error("平板上傳新增客戶PDF失敗: {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5312.getCode(), ResultCode.ERR_5312.getDesc());
		}
		return outVal;
	}
}
