package com.broton.enote.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.broton.enote.bo.FormAddBO;
import com.broton.enote.bo.FormIdBO;
import com.broton.enote.bo.FormInputAddBO;
import com.broton.enote.bo.FormInputIdBO;
import com.broton.enote.bo.FormInputUpdateBO;
import com.broton.enote.bo.FormUpdateBO;
import com.broton.enote.bo.QueryFormListAllBO;
import com.broton.enote.bo.QueryFormListBO;
import com.broton.enote.bo.SetFormShowBO;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.Form;
import com.broton.enote.entity.FormInput;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.FormInputRepository;
import com.broton.enote.repository.FormRepository;
import com.broton.enote.service.ErpService;
import com.broton.enote.service.FormService;
import com.broton.enote.utils.PageUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
//import com.broton.enote.dto.ErpCustResultListDto;
import com.broton.enote.dto.ErpPhraseResultListDto;
import com.broton.enote.dto.FormInputListResultDto;
import com.broton.enote.dto.FormListResultDto;
import com.broton.enote.dto.FormPdfDto;
import com.broton.enote.dto.Position;
import com.broton.enote.dto.RelationPositionDto;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FormServiceImpl implements FormService {

	@Autowired
	private FormRepository formRepository;
	@Autowired
	private FormInputRepository formInputRepository;
	@Autowired
	private ErpService erpService;
	
	@Override
	public Page<FormListResultDto> getFormList(QueryFormListBO qryBo) {
		Page<FormListResultDto> pageDto = null;
		try {
			Pageable pageable = PageUtils.getPageByParameter(qryBo.getStart(), qryBo.getLength(), null);
			pageDto = formRepository.getFormList(qryBo.getFormTypeId(), pageable);
			// 取表單類型(from ERP)
			List<ErpPhraseResultListDto> output = erpService.apiGetPhraseByPublic();
			// listDto 需作轉換的動作,不然直接取會報錯
			ObjectMapper mapper = new ObjectMapper();
			List<ErpPhraseResultListDto> dto = new ArrayList<ErpPhraseResultListDto>();		
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(output);				
			dto = Arrays.asList(mapper.readValue(json, ErpPhraseResultListDto[].class));
			
			for (FormListResultDto s:pageDto.getContent()) {
				for (ErpPhraseResultListDto e: dto) {
					if (e.getPhCode().equals(s.getFormTypeId())) {
						s.setFormTypeName(e.getPhName());
						break;
					}
				}
			}						
			log.info("取得表單資料成功: {}筆", pageDto.getContent().size());
		} catch (Exception e) {
			log.error("取得表單資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5701.getCode(), ResultCode.ERR_5701.getDesc());
		}
		return pageDto;
	}
	
	@Override
	public List<FormListResultDto> getFormListAll(QueryFormListAllBO qryBo) {
		List<FormListResultDto> listDto = new ArrayList<FormListResultDto>();
		try {
			listDto = formRepository.getFormListAll(qryBo.getFormTypeId());
			// 取表單類型(from ERP)
			List<ErpPhraseResultListDto> output = erpService.apiGetPhraseByPublic();
			// listDto 需作轉換的動作,不然直接取會報錯
			ObjectMapper mapper = new ObjectMapper();
			List<ErpPhraseResultListDto> dto = new ArrayList<ErpPhraseResultListDto>();		
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(output);				
			dto = Arrays.asList(mapper.readValue(json, ErpPhraseResultListDto[].class));
			
			for (FormListResultDto s: listDto) {
				for (ErpPhraseResultListDto e: dto) {
					if (e.getPhCode().equals(s.getFormTypeId())) {
						s.setFormTypeName(e.getPhName());
						break;
					}
				}
			}
			log.info("取得表單資料成功: {}筆", listDto.size());
		} catch (Exception e) {
			log.error("取得表單資料列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5701.getCode(), ResultCode.ERR_5701.getDesc());
		}
		return listDto;
	}
	
	@Override
	public FormPdfDto getFormPdf(FormIdBO qryBo) {
		FormPdfDto output = new FormPdfDto();
		try {
			Form form = formRepository.findById(qryBo.getId()).orElse(null);
			if (null != form) {
				output.setPdf(form.getFileContent());
				log.info("取得表單PDF成功");
			} else {
				log.error("取得表單PDF錯誤,表單資料不存在");
				throw new ErrorCodeException(ResultCode.ERR_5703.getCode(), ResultCode.ERR_5703.getDesc());
			}
		} catch (Exception e) {
			log.error("取得表單PDF錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5702.getCode(), ResultCode.ERR_5702.getDesc());
		}
		return output;
	}
	
	@Override
	public List<FormInputListResultDto> getFormInputList(FormIdBO qryBo) {
		List<FormInputListResultDto> output = new ArrayList<FormInputListResultDto>();
		try {
			output = formInputRepository.getFormInputList(qryBo.getId());
			ObjectMapper objectMapper = new ObjectMapper();
			for (FormInputListResultDto s : output) {
				Position p = objectMapper.readValue(s.getPositionStr(), Position.class);
				s.setPosition(p);
				if (StringUtils.hasText(s.getRelationPositionStr())) {
					try {
						if (StringUtils.hasText(s.getRelationPositionStr())) {
							RelationPositionDto r = objectMapper.readValue(s.getRelationPositionStr(), RelationPositionDto.class);
							s.setRelationPosition(r);
						}
					} catch (JsonMappingException e) {
						log.error("RelationPosition Json 物件轉換錯誤 {}", e.toString());
					}
				}	
			}
			log.info("取得表單控制項列表成功: {} 筆", output.size());
		} catch (Exception e) {
			log.error("取得表單控制項列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5704.getCode(), ResultCode.ERR_5704.getDesc());
		}
		return output;
	}
	
	@Override
	public void formAdd(FormAddBO addBo, String loginUser) {
		try {
			int count = formRepository.getFormNameCount(addBo.getFormName());
			if (count > 0) {
				log.error("新增表單錯誤,表單名稱重複:{}", addBo.getFormName());
				throw new ErrorCodeException(ResultCode.ERR_5709.getCode(), ResultCode.ERR_5709.getDesc());
			}
			Form form = new Form();
			form.setFormTypeId(addBo.getTypeId());
			form.setFormName(addBo.getFormName());
			form.setFileContent(Base64.decodeBase64(addBo.getPdfBase64()));
			form.setVersion(1);
			form.setShowFlag(1);
			form.setDoctorSign(addBo.getDoctorSign());
			form.setCreateUser(loginUser);
			form.setCreateDate(new Date());
			formRepository.save(form);
			log.info("新增表單成功");
		} catch (Exception e) {
			log.error("新增表單錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5705.getCode(), ResultCode.ERR_5705.getDesc());
		}
	}
	
	@Override
	public void formUpdate(FormUpdateBO updBo, String loginUser) {
		try {
			Form form = formRepository.findById(updBo.getId()).orElse(null);
			if (null != form) {
				form.setFormName(updBo.getFormName());
				form.setFileContent(Base64.decodeBase64(updBo.getPdfBase64()));
				form.setFormTypeId(updBo.getTypeId());
				form.setVersion(form.getVersion() + 1); // 版號自動 + 1
				form.setDoctorSign(updBo.getDoctorSign());
				form.setEditUser(loginUser);
				form.setEditDate(new Date());
				formRepository.save(form);
				log.info("更新表單成功");
			} else {
				log.error("更新表單錯誤,表單資料不存在  id:{}", updBo.getId());
				throw new ErrorCodeException(ResultCode.ERR_5703.getCode(), ResultCode.ERR_5703.getDesc());
			}
		} catch (Exception e) {
			log.error("更新表單錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5706.getCode(), ResultCode.ERR_5706.getDesc());
		}
	}
	
	@Override
	@Transactional
	public void formDelete(FormIdBO idBo) {
		try {
			if (formRepository.existsById(idBo.getId())) {
				formRepository.deleteById(idBo.getId());
				log.info("刪除表單成功 {}", idBo.getId());
				//一併刪除表單輸入項目
				formInputRepository.deleteByFormId(idBo.getId());
				log.info("刪除表單輸入項目成功");
			} else {
				log.error("刪除表單錯誤,表單資料不存在:{}", idBo.getId());
				throw new ErrorCodeException(ResultCode.ERR_5716.getCode(), ResultCode.ERR_5716.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("刪除表單錯誤:{}", e.toString());
			//手動強制回滾,不然會被 Exception 吃掉不起作用
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new ErrorCodeException(ResultCode.ERR_5715.getCode(), ResultCode.ERR_5715.getDesc());
		}
	}
	
	@Override
	public void setFormShow(SetFormShowBO setBo, String loginUser) {
		try {
			Form form = formRepository.findById(setBo.getId()).orElse(null);
			if (null != form) {
				form.setShowFlag(setBo.getShow());
				form.setEditUser(loginUser);
				form.setEditDate(new Date());
				formRepository.save(form);
				log.info("表單上/下架成功");
			} else {
				log.error("表單上/下架錯誤,表單資料不存在  id:{}", setBo.getId());
				throw new ErrorCodeException(ResultCode.ERR_5703.getCode(), ResultCode.ERR_5703.getDesc());
			}
		} catch (Exception e) {
			log.error("表單上/下架錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5707.getCode(), ResultCode.ERR_5707.getDesc());
		}
	}

	@Override
	public void formInputAdd(FormInputAddBO addBo, String loginUser) {
		try {
			FormInput formInput = new FormInput();
			BeanUtils.copyProperties(addBo, formInput);
			formInput.setCreateUser(loginUser);
			formInput.setCreateDate(new Date());
			formInputRepository.save(formInput);
			log.info("新增表單輸入項目成功");
		} catch (Exception e) {
			log.info("新增表單輸入項目失敗:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5710.getCode(), ResultCode.ERR_5710.getDesc());
		}
	}
	
	@Override
	public void formInputUpdate(FormInputUpdateBO updBo, String loginUser) {
		try {
			FormInput formInput = formInputRepository.findById(updBo.getFormInputId()).orElse(null);
			if (null != formInput) {
				formInput.setPageNo(updBo.getPageNo());
				formInput.setInputType(updBo.getInputType());
				formInput.setPosition(updBo.getPosition());
				formInput.setMemo(updBo.getMemo());
				formInput.setEditUser(loginUser);
				formInput.setEditDate(new Date());
				formInputRepository.save(formInput);
				log.info("表單輸入項目修改成功");
			} else {
				log.info("修改表單輸入項目失敗,表單輸入資料不存在:{}", updBo);
				throw new ErrorCodeException(ResultCode.ERR_5712.getCode(), ResultCode.ERR_5712.getDesc());
			}
		} catch (Exception e) {
			log.info("修改表單輸入項目失敗:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5711.getCode(), ResultCode.ERR_5711.getDesc());
		}
	}
	
	@Override
	public void formInputDelete(FormInputIdBO idBo) {
		try {
			if (formInputRepository.existsById(idBo.getId())) {
				formInputRepository.deleteById(idBo.getId());
				log.info("刪除表單輸入項目成功");
			} else {
				log.info("刪除表單輸入項目失敗,表單輸入資料不存在:{}", idBo.getId());
				throw new ErrorCodeException(ResultCode.ERR_5714.getCode(), ResultCode.ERR_5714.getDesc());
			}
		} catch (Exception e) {
			log.info("刪除表單輸入項目失敗:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5713.getCode(), ResultCode.ERR_5713.getDesc());
		}		
	}
}
