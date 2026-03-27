package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.broton.enote.dto.FormTypeResultListDto;
import com.broton.enote.entity.FormType;

@Repository
@Transactional
public interface FormTypeRepository extends JpaRepository<FormType, BigInteger> {

	/** 取得文件類別列表
	 * @return
	 */
	List<FormTypeResultListDto> getFormTypeList();
		
}
