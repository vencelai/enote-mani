package com.broton.enote.schedule;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.broton.enote.bo.UploadLogIdBO;
import com.broton.enote.dto.UploadLogResultListDto;
import com.broton.enote.entity.UploadLog;
import com.broton.enote.repository.UploadLogRepository;
import com.broton.enote.service.FormStageService;
import com.broton.enote.service.UploadLogService;
import lombok.extern.log4j.Log4j2;

/** 定時排程作業
 * 
 */
@Component
@Log4j2
public class TimeTask {
	
	@Value("${uploadLog.failAutoUpload}")	
    private String failAutoUpload; // 失敗是否自動重新上傳
	@Autowired
	private UploadLogService uploadLogService;
	@Autowired
	private UploadLogRepository uploadLogRepository;
	@Autowired
	private FormStageService formStageService;

	//@Scheduled(fixedDelay = 1000*60*15) // 啟動時執行一次, 之後每隔15分鐘執行一次
	@Scheduled(cron = "01 00 00 1/1 * ?") // 每日的 00:00:01 執行一次
	public void deleteExpiredUploadLog() {
		log.info("開始刪除超過保留天數的上傳日誌排程");
		uploadLogService.deleteExpiredUploadLog();
		log.info("結束刪除超過保留天數的上傳日誌排程");
		
		log.info("開始刪除超過保留期限的表單暫存資料(只針對棄用的)排程");
		formStageService.deleteExpiredFormStage();
		log.info("結束刪除超過保留期限的表單暫存資料(只針對棄用的)排程");
	}
	
	// 檢查上傳失敗次數 <3 需重新上傳的表單資料	
	@Scheduled(fixedDelay = 1000*60*1) // 啟動時執行一次, 之後每隔1分鐘執行一次
	public void retryUploadForm() {
		if (failAutoUpload.equals("y")) {
			List<UploadLogResultListDto> failList = new ArrayList<UploadLogResultListDto>();
			// 取出失敗的上傳資料
			failList = uploadLogService.getUploadLogFailList();
		
			for (UploadLogResultListDto s:failList) {
				UploadLog uploadLog = uploadLogRepository.findById(s.getId()).orElse(null);
				if (null != uploadLog) {
					if (uploadLog.getFailCount() < 3) {
						log.info("開始執行每分鐘自動重新上傳機制 id:{}", s.getId());
						UploadLogIdBO idBo = new UploadLogIdBO(); 
						idBo.setId(s.getId());
						uploadLogService.reUploadPdf(idBo);
					}
				}
			}
		}
	}
	
	// 檢查上傳失敗次數 >= 3 需重新上傳的表單資料
	@Scheduled(fixedDelay = 1000*60*240) // 啟動時執行一次, 之後每隔4小時執行一次
	public void retryUploadFormByFourHour() {
		if (failAutoUpload.equals("y")) {
			List<UploadLogResultListDto> failList = new ArrayList<UploadLogResultListDto>();
			// 取出失敗次數 >=3 的上傳資料
			failList = uploadLogService.getUploadLogFailThirdList();
		
			for (UploadLogResultListDto s:failList) {
				UploadLog uploadLog = uploadLogRepository.findById(s.getId()).orElse(null);
				if (null != uploadLog) {
					log.info("開始執行4小時自動重新上傳機制 id:{}", s.getId());
					UploadLogIdBO idBo = new UploadLogIdBO(); 
					idBo.setId(s.getId());
					uploadLogService.reUploadPdf(idBo);
				}
			}
		}
	}
}
