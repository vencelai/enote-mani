package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.broton.enote.dto.FormInputListResultDto;
import com.broton.enote.entity.FormInput;

@Repository
@Transactional
public interface FormInputRepository extends JpaRepository<FormInput, BigInteger> {

	/** 取得表單輸入項目列表
	 * @param formId
	 * @return
	 */
	List<FormInputListResultDto> getFormInputList(BigInteger formId);
	
	/** 刪除表單輸入項目 by 表單 ID
	 * @param formId
	 */
	@Modifying
	@Query(value = "Delete From form_input Where form_id = :formId ", nativeQuery = true)
	void deleteByFormId(@Param("formId") BigInteger formId);
		
}
