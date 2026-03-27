package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.broton.enote.bo.QueryUploadLogListBO;
import com.broton.enote.dto.UploadLogResultListDto;
import com.broton.enote.entity.UploadLog;

@Repository
@Transactional
public interface UploadLogRepository extends JpaRepository<UploadLog, BigInteger> {

	/** 取得上傳日誌列表
	 * @return
	 */
	Page<UploadLogResultListDto> getUploadLogList(QueryUploadLogListBO qryBo, Pageable pageable);
	
	/** 取得上傳失敗次數 <3的日誌列表
	 * @return
	 */
	List<UploadLogResultListDto> getUploadLogFailList();
	
	/** 取得上傳失敗次數>3的日誌列表
	 * @return
	 */
	List<UploadLogResultListDto> getUploadLogFailThirdList();
	
	// 刪除超過保留期限的上傳日誌資料
	@Modifying
	@Query(value = "Delete From upload_log Where Date(create_date) <= Date(DATE_SUB(NOW(), INTERVAL :reserveDay day))", nativeQuery = true)
	void deleteExpiredUploadLog(@Param("reserveDay") Integer reserveDay);
		
}
