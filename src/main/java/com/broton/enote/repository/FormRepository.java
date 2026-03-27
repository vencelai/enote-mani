package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.broton.enote.dto.FormListResultDto;
import com.broton.enote.entity.Form;

@Repository
@Transactional
public interface FormRepository extends JpaRepository<Form, BigInteger> {

	/** 取得表單資料列表
	 * @param formTypeId
	 * @param pageable
	 * @return
	 */
	Page<FormListResultDto> getFormList(String formTypeId, Pageable pageable);
	
	/** 取得表單資料列表
	 * @param formTypeId
	 * @param pageable
	 * @return
	 */
	List<FormListResultDto> getFormListAll(String formTypeId);
	
	/** 取得是否已有同名稱的表單
	 * @param formName
	 * @return
	 */
	@Query(value = "Select Count(*) From form Where form_name = :formName ", nativeQuery = true)
	int getFormNameCount(@Param("formName") String formName);
		
}
