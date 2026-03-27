package com.broton.enote.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.broton.enote.bo.CustomerIdNameBO;
import com.broton.enote.bo.EmployeeIdBO;
import com.broton.enote.bo.ErpCustBO;
import com.broton.enote.bo.ErpGetStaffsByPublicBO;
import com.broton.enote.bo.StageIdBO;
import com.broton.enote.bo.UploadStageBO;
import com.broton.enote.dto.ErpCustResultListDto;
import com.broton.enote.dto.ErpDoctorResultListDto;
import com.broton.enote.dto.StageDto;
import com.broton.enote.dto.StageGroupDto;
import com.broton.enote.dto.StageListDto;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.Stage;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.StageRepository;
import com.broton.enote.service.ErpService;
import com.broton.enote.service.StageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class StageServiceImpl implements StageService {

	@Autowired
	private StageRepository stageRepository;
	@Autowired
	private ErpService erpService;
	
	@Override
	public void uploadStage(UploadStageBO updBo) {
		Stage stage = new Stage();
		try {
			if (null == updBo.getStageId()) {
				// 新增
				List<ErpCustResultListDto> listDto = new ArrayList<ErpCustResultListDto>();
				String customerName = "";
				// 打 Erp api 取回客戶姓名
				ErpCustBO qryBo = new ErpCustBO();
				qryBo.setCustFindStr(updBo.getCustomerId());
				listDto = erpService.apiGetCustByPublic(qryBo);
				if (listDto.size() > 0) {
					//log.info(listDto);
					// listDto 需作轉換的動作,不然直接取會報錯
					ObjectMapper mapper = new ObjectMapper();
					List<ErpCustResultListDto> dto = new ArrayList<ErpCustResultListDto>();		
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(listDto);				
					dto = Arrays.asList(mapper.readValue(json, ErpCustResultListDto[].class));
					customerName = dto.get(0).getCustCname(); // 客戶姓名
				}				
				stage.setFormId(updBo.getFormId());
				stage.setSignTrack(Base64.decodeBase64(updBo.getSignTrack()));
				stage.setCustomerId(updBo.getCustomerId());
				stage.setCustomerName(customerName);
				stage.setEmployeeId(updBo.getEmployeeId());
				stage.setEmployeeName(updBo.getEmployeeName());
				stage.setCreateDate(updBo.getCreateDate());
				stage.setStageDate(new Date());
				stageRepository.save(stage);
			} else {
				// 修改
				stage = stageRepository.findById(updBo.getStageId()).orElse(null);
				if (null != stage) {
					stage.setFormId(updBo.getFormId());
					stage.setSignTrack(Base64.decodeBase64(updBo.getSignTrack()));
					stage.setCustomerId(updBo.getCustomerId());
					stage.setEmployeeId(updBo.getEmployeeId());
					stage.setEmployeeName(updBo.getEmployeeName());
					stage.setStageDate(new Date());
					stageRepository.save(stage);
				} else {
					log.error("上傳暫存表單失敗,查無暫存表單資料 id:{}", updBo.getStageId());
					throw new ErrorCodeException(ResultCode.ERR_5982.getCode(), ResultCode.ERR_5982.getDesc());
				}
			}
			log.info("上傳暫存表單成功");
		} catch (Exception e) {
			log.error("上傳暫存表單錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5981.getCode(), ResultCode.ERR_5981.getDesc());
		}
	}
	
	@Override
	public List<StageGroupDto> getStageGroup(EmployeeIdBO idBo) {
		List<StageGroupDto> output = new ArrayList<StageGroupDto>();
		try {
			output = stageRepository.getStageGroup(idBo);
			log.info("取得暫存表單群組成功 {} 筆", output.size());
		} catch (Exception e) {
			log.error("取得暫存表單群組錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5983.getCode(), ResultCode.ERR_5983.getDesc());
		}
		return output;
	}
	
	@Override
	public List<StageListDto> getStageGroupList(CustomerIdNameBO idNameBo) {
		List<StageListDto> output = new ArrayList<StageListDto>();
		try {
			output = stageRepository.getStageGroupList(idNameBo);			
			for (StageListDto s:output) {
				List<ErpDoctorResultListDto> doctorList = new ArrayList<ErpDoctorResultListDto>();
				ErpGetStaffsByPublicBO qryBo = new ErpGetStaffsByPublicBO();
				qryBo.setStfNo(s.getDoctorId());
				doctorList = erpService.apiGetStaffsByPublic(qryBo);
				if (doctorList.size() > 0) {
					//log.info(listDto);
					// listDto 需作轉換的動作,不然直接取會報錯
					ObjectMapper mapper = new ObjectMapper();
					List<ErpDoctorResultListDto> dto = new ArrayList<ErpDoctorResultListDto>();		
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(doctorList);				
					dto = Arrays.asList(mapper.readValue(json, ErpDoctorResultListDto[].class));
					s.setDoctorName(dto.get(0).getStfName());
				}
			}
			
			log.info("取得暂存表單清單成功 {} 筆", output.size());
		} catch (Exception e) {
			log.error("取得暂存表單清單錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5984.getCode(), ResultCode.ERR_5984.getDesc());
		}
		return output;
	}
	
	@Override
	public StageDto getStageById(StageIdBO idBo) {
		StageDto output = new StageDto();
		try {
			output = stageRepository.getStageById(idBo);
			// 打 Erp api 取回客戶相關資訊
			List<ErpCustResultListDto> listDto = new ArrayList<ErpCustResultListDto>();
			ErpCustBO qryBo = new ErpCustBO();
			qryBo.setCustFindStr(output.getCustomerId());
			listDto = erpService.apiGetCustByPublic(qryBo);
			if (listDto.size() > 0) {
				// listDto 需作轉換的動作,不然直接取會報錯
				ObjectMapper mapper = new ObjectMapper();
				List<ErpCustResultListDto> dto = new ArrayList<ErpCustResultListDto>();		
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(listDto);				
				dto = Arrays.asList(mapper.readValue(json, ErpCustResultListDto[].class));
				output.setCustomerTel(dto.get(0).getGsmtel());
				output.setCustomerBirthday(dto.get(0).getBirthday());
				if (null != dto.get(0).getBirthday()) {
					output.setRcYY(String.valueOf(Integer.valueOf(birthdayFormat(dto.get(0).getBirthday(), "yyyy"))-1911));
					output.setRcMM(birthdayFormat(dto.get(0).getBirthday(), "MM"));
					output.setRcDD(birthdayFormat(dto.get(0).getBirthday(), "dd"));
				}
			}
			
			// 打 Erp api 取回醫師資訊
			List<ErpDoctorResultListDto> doctorList = new ArrayList<ErpDoctorResultListDto>();
			ErpGetStaffsByPublicBO bo = new ErpGetStaffsByPublicBO();
			bo.setStfNo(output.getDoctorId());
			doctorList = erpService.apiGetStaffsByPublic(bo);
			if (doctorList.size() > 0) {
				// doctorList 需作轉換的動作,不然直接取會報錯
				ObjectMapper mapper = new ObjectMapper();
				List<ErpDoctorResultListDto> dto = new ArrayList<ErpDoctorResultListDto>();		
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(doctorList);				
				dto = Arrays.asList(mapper.readValue(json, ErpDoctorResultListDto[].class));
				output.setDoctorName(dto.get(0).getStfName());
			}
			log.info("取得暫存表單成功 id: {}", idBo.getId());
		} catch (Exception e) {
			log.error("取得暂存表單錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5985.getCode(), ResultCode.ERR_5985.getDesc());
		}
		
		return output;
	}
	
	@SuppressWarnings("deprecation")
	public String birthdayFormat(String birthday, String format) {
		String output = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			output = simpleDateFormat.format(new Date(birthday));
		} catch (Exception e) {
			log.error("日期格式轉換錯誤 {} {}", birthday, e.toString());
		}
		return output;
    }
}
