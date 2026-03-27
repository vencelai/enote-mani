package com.broton.enote.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.BranchService;
import com.broton.enote.service.DeviceService;
import com.broton.enote.service.DoctorService;
import com.broton.enote.service.ErpService;
import com.broton.enote.service.FormService;
import com.broton.enote.service.FormStageService;
//import com.broton.enote.service.FormTypeService;
import com.broton.enote.service.PadService;
import com.broton.enote.utils.DtoUtils;
import com.broton.enote.bo.DoctorStfNoBO;
import com.broton.enote.bo.ErpCustBO;
import com.broton.enote.bo.ErpCustSaveListBO;
import com.broton.enote.bo.ErpGetStaffsBO;
import com.broton.enote.bo.ErpLoginBO;
import com.broton.enote.bo.ErpPhraseBO;
import com.broton.enote.bo.FormIdBO;
import com.broton.enote.bo.FormStageIdBO;
import com.broton.enote.bo.NewCustomerBO;
import com.broton.enote.bo.PdfDownloadBO;
import com.broton.enote.bo.PdfListBO;
import com.broton.enote.bo.PdfStageBO;
import com.broton.enote.bo.PdfUploadBO;
import com.broton.enote.bo.QueryFormListAllBO;
import com.broton.enote.bo.QueryFormStageListBO;
import com.broton.enote.bo.SendDeviceInfoBO;
import com.broton.enote.bo.SetFormStageLockBO;
import com.broton.enote.dto.BranchListResultDto;
import com.broton.enote.dto.ErpCustResultListDto;
import com.broton.enote.dto.ErpCustSaveResultListDto;
import com.broton.enote.dto.ErpDoctorResultListDto;
import com.broton.enote.dto.ErpPdfListResultDto;
import com.broton.enote.dto.ErpPhraseResultListDto;
import com.broton.enote.dto.ErpTokenResultDto;
import com.broton.enote.dto.FormListResultDto;
import com.broton.enote.dto.FormPdfAndPosition;
import com.broton.enote.dto.FormStageResultListDto;
import com.broton.enote.dto.PdfSignTrackDto;
import com.broton.enote.dto.SetDeviceInfoReturnDto;

@Log4j2
@Api(value = "平板相關 API", description = "平板相關 API")
@RestController
@RequestMapping("/pad/api")
public class PadController {
	
	@Autowired
	private PadService padService;
	//@Autowired
	//private FormTypeService formTypeService;
	@Autowired
	private FormService formService;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private ErpService erpService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private BranchService branchService;
	//@Autowired
	//private StageService stageService;
	@Autowired
	private FormStageService formStageService;
	
	@ApiOperation(value = "員工登入", httpMethod = "POST", notes = "員工登入")
	@PostMapping("/employeeLogin")
	public ResponseEntity<ResponseDto<Object>> employee(@RequestBody(required = true) @Valid @ApiParam(value = "員工登入物件") ErpLoginBO loginBO) {
		log.info("開始員工登入");
		ErpTokenResultDto output = erpService.apiLogin(loginBO);
		log.info("結束員工登入");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	/*
	@ApiOperation(value = "取得文件類別列表", httpMethod = "POST")
	@PostMapping("/getFormTypeList")
	public ResponseEntity<ResponseDto<Object>> getFormTypeList() {
		log.info("開始取得文件類別列表");
		List<FormTypeResultListDto> output = formTypeService.getFormTypeList();
		log.info("結束取得文件類別列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	*/
	
	@ApiOperation(value = "取得表單資料列表", httpMethod = "POST")
	@PostMapping("/getFormListAll")
	public ResponseEntity<ResponseDto<Object>> getFormListAll(@RequestBody(required = true) @Valid @ApiParam(value = "表單資料列表查詢物件") QueryFormListAllBO qryBo) {
		log.info("開始取得表單資料列表");
		List<FormListResultDto> listFormResultDto = null;
		listFormResultDto = formService.getFormListAll(qryBo);
		log.info("結束取得表單資料列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(listFormResultDto);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得客戶列表", httpMethod = "POST")
	@PostMapping("/getCustList")
	public ResponseEntity<ResponseDto<Object>> getCustList(@RequestBody(required = true) @Valid @ApiParam(value = "取得客戶列表查詢物件") ErpCustBO qryBo) {
		log.info("開始取得客戶列表");
		List<ErpCustResultListDto> output = erpService.apiCust(qryBo);
		log.info("結束取得客戶列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單分類列表", httpMethod = "POST")
	@PostMapping("/getPhraseList")
	public ResponseEntity<ResponseDto<Object>> getPhraseList(@RequestBody(required = true) @Valid @ApiParam(value = "取得表單分類列表查詢物件") ErpPhraseBO qryBo) {
		log.info("開始取得表單分類列表");
		List<ErpPhraseResultListDto> output = erpService.apiGetPhrase(qryBo);
		log.info("結束取得表單分類列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得醫師列表", httpMethod = "POST")
	@PostMapping("/getDoctorList")
	public ResponseEntity<ResponseDto<Object>> getDoctorList(@RequestBody(required = true) @Valid @ApiParam(value = "取得醫師列表查詢物件") ErpGetStaffsBO qryBo) {
		log.info("開始取得醫師列表");
		List<ErpDoctorResultListDto> output = erpService.apiGetStaffs(qryBo);
		log.info("結束取得醫師列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得醫師印章", httpMethod = "POST")
	@PostMapping("/getDoctorStamp")
	public ResponseEntity<ResponseDto<Object>> getDoctorStamp(@RequestBody(required = true) @Valid @ApiParam(value = "醫師代號物件") DoctorStfNoBO idBo) {
		log.info("開始取得醫師印章");
		String output = doctorService.getDoctorStamp(idBo);
		log.info("結束取得醫師印章");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單Pdf&座標資訊", httpMethod = "POST")
	@PostMapping("/getFormPdfAndPosition")
	public ResponseEntity<ResponseDto<Object>> getFormPdfAndPosition(@RequestBody(required = true) @Valid @ApiParam(value = "表單ID物件") FormIdBO qryBo) {
		log.info("開始取得表單pdf&座標資訊");
		FormPdfAndPosition output = padService.getFormPdfAndPosition(qryBo);
		log.info("結束取得表單pdf&座標資訊");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "接收板子發送的設備狀態", httpMethod = "POST")
	@PostMapping("/setDeviceInfo")
	public ResponseEntity<ResponseDto<Object>> setDeviceInfo(@RequestBody(required = true) @Valid @ApiParam(value = "設備狀態物件") SendDeviceInfoBO qryBo) {
		log.info("開始接收板子發送的設備狀態");
		SetDeviceInfoReturnDto output = deviceService.setDeviceInfo(qryBo);
		// 平板收到回傳的dto若不為null時需替換掉自己記錄的branchId
		log.info("結束接收板子發送的設備狀態");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得據點列表", httpMethod = "POST")
	@PostMapping("/getBranchList")
	public ResponseEntity<ResponseDto<Object>> getBranchList() {
		log.info("開始取得據點列表");
		List<BranchListResultDto> output = branchService.getBranchList();
		log.info("結束取得據點列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得客戶已簽署PDF清單", httpMethod = "POST")
	@PostMapping("/apiPdfList")
	public ResponseEntity<ResponseDto<Object>> apiPdfList(@RequestBody(required = true) @Valid @ApiParam(value = "取得客戶已簽署PDF清單查詢物件") PdfListBO qryBo) {
		log.info("開始取得客戶已簽署PDF清單");
		List<ErpPdfListResultDto> output = erpService.apiPdfList(qryBo);
		log.info("結束取得客戶已簽署PDF清單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "上傳新的客戶簽署PDF", httpMethod = "POST")
	@PostMapping("/apiPdfUpload")
	public ResponseEntity<ResponseDto<Object>> apiPdfUpload(@RequestBody(required = true) @Valid @ApiParam(value = "上傳新的客戶簽署PDF物件") PdfUploadBO uploadBo) {
		log.info("開始上傳新的客戶簽署PDF");
		erpService.apiPdfUpload(uploadBo);
		log.info("結束上傳新的客戶簽署PDF");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "以紀錄UNID下載特定PDF檔案", httpMethod = "POST")
	@PostMapping("/apiPdfDownload")
	public ResponseEntity<ResponseDto<Object>> apiPdfDownload(@RequestBody(required = true) @Valid @ApiParam(value = "紀錄UNID下載特定PDF檔案物件") PdfDownloadBO downloadBo) {
		log.info("開始以紀錄UNID下載特定PDF檔案");
		String output = erpService.apiPdfDownload(downloadBo);
		log.info("結束以紀錄UNID下載特定PDF檔案");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "以紀錄UNID下載特定PDF檔案 Test", httpMethod = "POST")
	@PostMapping("/apiPdfDownloadTest")
	public ResponseEntity<Resource> apiPdfDownloadTest(@RequestBody(required = true) @Valid @ApiParam(value = "紀錄UNID下載特定PDF檔案物件") PdfDownloadBO downloadBo) {
		log.info("開始以紀錄UNID下載特定PDF檔案");
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customer.pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStream rr = erpService.apiPdfDownloadTest(downloadBo);
        InputStreamResource resource = new InputStreamResource(rr);
        int len = 0;
		try {
			len = rr.available();
		} catch (IOException e) {
		}
		log.info("結束以紀錄UNID下載特定PDF檔案");
		return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(len)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}
	
	@ApiOperation(value = "依客戶編號查詢剩餘課程資料", httpMethod = "POST")
	@PostMapping("/apiCustSaveList")
	public ResponseEntity<ResponseDto<Object>> apiCustSaveList(@RequestBody(required = true) @Valid @ApiParam(value = "依客戶編號查詢剩餘課程資料物件") ErpCustSaveListBO qryBo) {
		log.info("開始依客戶編號查詢剩餘課程資料");
		List<ErpCustSaveResultListDto> output = erpService.apiCustSaveList(qryBo);
		log.info("結束依客戶編號查詢剩餘課程資料");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	/*
	@ApiOperation(value = "上傳暫存表單", httpMethod = "POST")
	@PostMapping("/uploadStage")
	public ResponseEntity<ResponseDto<Object>> uploadStage(@RequestBody(required = true) @Valid @ApiParam(value = "上傳暫存表單資料物件") UploadStageBO updBo) {
		log.info("開始上傳暫存表單");
		stageService.uploadStage(updBo);
		log.info("結束上傳暫存表單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}	
	@ApiOperation(value = "取得暫存表單群組", httpMethod = "POST")
	@PostMapping("/getStageGroup")
	public ResponseEntity<ResponseDto<Object>> getStageGroup(@RequestBody(required = true) @Valid @ApiParam(value = "取得暫存表單群組物件") EmployeeIdBO idBo) {
		log.info("開始取得暫存表單群組");
		List<StageGroupDto> output = stageService.getStageGroup(idBo);
		log.info("結束取得暫存表單群組");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得暂存表單清單", httpMethod = "POST")
	@PostMapping("/getStageGroupList")
	public ResponseEntity<ResponseDto<Object>> getStageGroupList(@RequestBody(required = true) @Valid @ApiParam(value = "取得暂存表單清單物件") CustomerIdNameBO idNameBo) {
		log.info("開始取得暂存表單清單");
		List<StageListDto> output = stageService.getStageGroupList(idNameBo);
		log.info("結束取得暂存表單清單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "依據暫存表單id取得暂存表單資料", httpMethod = "POST")
	@PostMapping("/getStageById")
	public ResponseEntity<ResponseDto<Object>> getStageById(@RequestBody(required = true) @Valid @ApiParam(value = "取得暂存表單清單物件") StageIdBO idBo) {
		log.info("開始依據暫存表單id取得暂存表單資料");
		StageDto output = stageService.getStageById(idBo);
		log.info("結束依據暫存表單id取得暂存表單資料");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	*/
	@ApiOperation(value = "平板上傳log檔案", httpMethod = "POST")
	@PostMapping("/uploadPadLog")
	public ResponseEntity<ResponseDto<Object>> uploadPadLog(@RequestParam("file") MultipartFile file) {
		log.info("開始平板上傳log檔案");		
		padService.uploadPadLog(file);
		log.info("結束平板上傳log檔案");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "平板新增客戶", httpMethod = "POST")
	@PostMapping("/newCustomer")
	public ResponseEntity<ResponseDto<Object>> newCustomer(@RequestBody(required = true) @Valid @ApiParam(value = "平板新增客戶物件") NewCustomerBO bo) {
		log.info("開始平板新增客戶");
		erpService.newCustomer(bo);
		log.info("結束平板新增客戶");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單暫存資料列表", httpMethod = "POST")
	@PostMapping("/getFormStageList")
	public ResponseEntity<ResponseDto<Object>> getFormStageList(@RequestBody(required = true) @Valid @ApiParam(value = "取得表單暫存資料列表物件") QueryFormStageListBO bo) {
		log.info("開始取得表單暫存資料列表");
		List<FormStageResultListDto> output = formStageService.getFormStageList(bo);
		log.info("結束取得表單暫存資料列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單暫存資料列表(分頁)", httpMethod = "POST")
	@PostMapping("/getFormStagePageList")
	public ResponseEntity<ResponseDto<Object>> getFormStagePageList(@RequestBody(required = true) @Valid @ApiParam(value = "取得表單暫存資料列表物件") QueryFormStageListBO bo) {
		log.info("開始取得表單暫存資料列表(分頁)");
		Page<FormStageResultListDto> output = formStageService.getFormStagePageList(bo);
		log.info("結束取得表單暫存資料列表(分頁)");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "由流水號取得表單暫存資料", httpMethod = "POST")
	@PostMapping("/getFormStageById")
	public ResponseEntity<ResponseDto<Object>> getFormStageById(@RequestBody(required = true) @Valid @ApiParam(value = "由流水號取得表單暫存資料物件") FormStageIdBO bo) {
		log.info("開始由流水號取得表單暫存資料");
		FormStageResultListDto output = formStageService.getFormStageById(bo);
		log.info("結束由流水號取得表單暫存資料");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "新增/修改暫存表單", httpMethod = "POST")
	@PostMapping("/formStageImport")
	public ResponseEntity<ResponseDto<Object>> formStageImport(@RequestBody(required = true) @Valid @ApiParam(value = "新增/修改暫存表單物件") PdfStageBO bo) {
		log.info("開始新增/修改暫存表單");
		formStageService.formStageImport(bo);
		log.info("結束新增/修改暫存表單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單暫存 PDF base64, 簽名軌跡 base64", httpMethod = "POST")
	@PostMapping("/getFormStagePdfSignTrack")
	public ResponseEntity<ResponseDto<Object>> getFormStagePdfSignTrack(@RequestBody(required = true) @Valid @ApiParam(value = "由流水號取得表單暫存資料物件") FormStageIdBO bo) {
		log.info("開始取得表單暫存 PDF base64, 簽名軌跡 base64");
		PdfSignTrackDto output = formStageService.getFormStagePdfSignTrack(bo);
		log.info("結束取得表單暫存 PDF base64, 簽名軌跡 base64");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "表單暫存棄用", httpMethod = "POST")
	@PostMapping("/terminationFormStage")
	public ResponseEntity<ResponseDto<Object>> terminationFormStage(@RequestBody(required = true) @Valid @ApiParam(value = "表單暫存棄用物件") FormStageIdBO bo) {
		log.info("開始表單暫存棄用");
		formStageService.terminationFormStage(bo);
		log.info("結束表單暫存棄用");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "解除表單暫存鎖定(後台使用)", httpMethod = "POST")
	@PostMapping("/abortFormStageLock")
	public ResponseEntity<ResponseDto<Object>> abortFormStageLock(@RequestBody(required = true) @Valid @ApiParam(value = "解除表單暫存鎖定物件") FormStageIdBO bo) {
		log.info("開始解除表單暫存鎖定");
		formStageService.abortFormStageLock(bo);
		log.info("結束解除表單暫存鎖定");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "設定表單暫存鎖定", httpMethod = "POST")
	@PostMapping("/setFormStageLock")
	public ResponseEntity<ResponseDto<Object>> setFormStageLock(@RequestBody(required = true) @Valid @ApiParam(value = "設定表單暫存鎖定物件") SetFormStageLockBO bo) {
		log.info("開始設定表單暫存鎖定");
		formStageService.setFormStageLock(bo);
		log.info("結束設定表單暫存鎖定");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "上傳暫存表單到 erp", httpMethod = "POST")
	@PostMapping("/formStageUploadToErp")
	public ResponseEntity<ResponseDto<Object>> formStageUploadToErp(@RequestBody(required = true) @Valid @ApiParam(value = "表單暫存棄用物件") PdfStageBO bo) {
		log.info("開始上傳暫存表單到 erp");
		formStageService.formStageUploadToErp(bo);
		log.info("結束上傳暫存表單到 erp");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
}
