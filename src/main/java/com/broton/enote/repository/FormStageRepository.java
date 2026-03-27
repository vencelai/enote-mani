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

import com.broton.enote.bo.FormStageIdBO;
import com.broton.enote.bo.QueryFormStageListBO;
import com.broton.enote.dto.FormStageResultListDto;
import com.broton.enote.entity.FormStage;

@Repository
@Transactional
public interface FormStageRepository extends JpaRepository<FormStage, BigInteger> {

	/** 取得暫存表單列表
	 * @return
	 */
	List<FormStageResultListDto> getFormStageList(QueryFormStageListBO qryBo);
	
	/** 取得暫存表單列表
	 * @return
	 */
	Page<FormStageResultListDto> getFormStagePageList(QueryFormStageListBO qryBo, Pageable pageable);

	FormStageResultListDto getFormStageById(FormStageIdBO idBo);
	
	// 刪除超過保留期限的表單暫存資料(只針對棄用的)
	@Modifying
	@Query(value = "Delete From form_stage Where termination = 'y' and Date(create_date) <= Date(DATE_SUB(NOW(), INTERVAL :reserveDay day))", nativeQuery = true)
	void deleteExpiredFormStage(@Param("reserveDay") Integer reserveDay);
}
